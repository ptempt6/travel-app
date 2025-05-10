import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Modal, message, Select, Divider } from 'antd';
import { RouteRequestDto, RouteResponseDto, PlaceResponseDto, UserResponseDto } from '../../types/entities';
import { createRoute, updateRoute, addPlaceToRoute, removePlaceFromRoute } from '../../api/routeApi';
import { getAllPlaces } from '../../api/placeApi';
import { getUserById, getAllUsers } from '../../api/userApi';

interface RouteFormProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
  action: 'create' | 'edit';
  route: RouteResponseDto | null;
}

const RouteForm: React.FC<RouteFormProps> = ({ 
  visible, 
  onCancel, 
  onSuccess,
  action,
  route 
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [places, setPlaces] = useState<PlaceResponseDto[]>([]);
  const [users, setUsers] = useState<UserResponseDto[]>([]);
  const [selectedPlace, setSelectedPlace] = useState<number | null>(null);
  const [placesLoading, setPlacesLoading] = useState(false);
  const [usersLoading, setUsersLoading] = useState(false);
  const [authorName, setAuthorName] = useState('');

  useEffect(() => {
    if (visible) {
      if (action === 'edit') {
        fetchAllPlaces();
        if (route?.authorId) {
          fetchAuthorName(route.authorId);
        }
      } else if (action === 'create') {
        fetchAllUsers();
      }
      
      if (route) {
        form.setFieldsValue({
          name: route.name,
          description: route.description,
          authorId: route.authorId,
        });
      } else {
        form.resetFields();
      }
    }
  }, [visible, route, action, form]);

  const fetchAllPlaces = async () => {
    setPlacesLoading(true);
    try {
      const data = await getAllPlaces();
      setPlaces(data);
    } catch (error) {
      message.error('Failed to fetch places');
    } finally {
      setPlacesLoading(false);
    }
  };

  const fetchAllUsers = async () => {
    setUsersLoading(true);
    try {
      const data = await getAllUsers();
      setUsers(data);
    } catch (error) {
      message.error('Failed to fetch users');
    } finally {
      setUsersLoading(false);
    }
  };

  const fetchAuthorName = async (userId: number) => {
    try {
      const user = await getUserById(userId);
      setAuthorName(user.name);
    } catch (error) {
      message.error('Failed to fetch author name');
      setAuthorName(`User #${userId}`);
    }
  };

  const handleSubmit = async (values: RouteRequestDto) => {
    setLoading(true);
    try {
      if (action === 'create') {
        await createRoute(values);
        message.success('Route created successfully');
      } else if (route) {
        const changedFields = form.getFieldsValue();
        await updateRoute(route.id, {
          ...route,
          ...changedFields
        });
        message.success('Route updated successfully');
      }
      onSuccess();
    } catch (error) {
      message.error(`Failed to ${action === 'create' ? 'create' : 'update'} route`);
    } finally {
      setLoading(false);
    }
  };

  const handleAddPlace = async () => {
    if (!selectedPlace || !route) return;
    
    try {
      await addPlaceToRoute(route.id, selectedPlace);
      message.success('Place added to route successfully');
      onSuccess();
      setSelectedPlace(null);
    } catch (error) {
      message.error('Failed to add place to route');
    }
  };

  const handleRemovePlace = async (placeId: number) => {
    if (!route) return;
    
    try {
      await removePlaceFromRoute(route.id, placeId);
      message.success('Place removed from route successfully');
      onSuccess();
    } catch (error) {
      message.error('Failed to remove place from route');
    }
  };

  return (
    <Modal
      visible={visible}
      title={action === 'create' ? 'Create Route' : 'Edit Route'}
      onCancel={onCancel}
      footer={null}
      width={800}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
      >
        <Form.Item
          name="name"
          label="Name"
          rules={[{ required: true, message: 'Please input the route name!' }]}
        >
          <Input disabled={action === 'edit'} />
        </Form.Item>
        
        <Form.Item
          name="description"
          label="Description"
          rules={[{ required: true, message: 'Please input the route description!' }]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>
        
        {action === 'create' ? (
          <Form.Item
            name="authorId"
            label="Author"
            rules={[{ required: true, message: 'Please select an author!' }]}
          >
            <Select
              loading={usersLoading}
              placeholder="Select author"
              options={users.map(user => ({
                value: user.id,
                label: user.name
              }))}
            />
          </Form.Item>
        ) : (
          <Form.Item
            label="Author"
          >
            <Input 
              value={authorName || 'Loading...'} 
              disabled 
            />
          </Form.Item>
        )}

        {action === 'edit' && route && (
          <>
            <Divider orientation="left">Manage Places</Divider>
            <div style={{ marginBottom: 16 }}>
              <Select
                style={{ width: '60%' }}
                placeholder="Select place to add"
                loading={placesLoading}
                value={selectedPlace}
                onChange={setSelectedPlace}
                options={places
                  .filter(p => !route.places?.some(rp => rp.id === p.id))
                  .map(p => ({ value: p.id, label: p.name }))
                }
              />
              <Button 
                type="primary" 
                onClick={handleAddPlace}
                disabled={!selectedPlace}
                style={{ marginLeft: 8 }}
              >
                Add Place
              </Button>
            </div>
            
            <div>
              <h4>Current Places:</h4>
              {route.places?.length ? (
                <ul>
                  {route.places.map(place => (
                    <li key={place.id} style={{ marginBottom: 8 }}>
                      {place.name}
                      <Button 
                        danger 
                        size="small" 
                        onClick={() => handleRemovePlace(place.id)}
                        style={{ marginLeft: 8 }}
                      >
                        Remove
                      </Button>
                    </li>
                  ))}
                </ul>
              ) : (
                <p>No places in this route</p>
              )}
            </div>
          </>
        )}

        <Form.Item style={{ marginTop: 24 }}>
          <Button type="primary" htmlType="submit" loading={loading}>
            {action === 'create' ? 'Create' : 'Update'}
          </Button>
          <Button style={{ marginLeft: 8 }} onClick={onCancel}>
            Cancel
          </Button>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default RouteForm;