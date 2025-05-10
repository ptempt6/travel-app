import React, { useState, useEffect } from 'react';
import { Table, Button, Space, message, Popconfirm } from 'antd';
import { PlaceResponseDto } from '../../types/entities';
import { getAllPlaces, deletePlace } from '../../api/placeApi';
import PlaceModal from '../EntityModal/PlaceModal';
import PlaceForm from './PlaceForm';

const PlacesTable: React.FC = () => {
  const [places, setPlaces] = useState<PlaceResponseDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedPlace, setSelectedPlace] = useState<PlaceResponseDto | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [formAction, setFormAction] = useState<'create' | 'edit'>('create');

  useEffect(() => {
    fetchPlaces();
  }, []);

  const fetchPlaces = async () => {
    setLoading(true);
    try {
      const data = await getAllPlaces();
      setPlaces(data);
    } catch (error) {
      message.error('Failed to fetch places');
    } finally {
      setLoading(false);
    }
  };

  const handleView = (place: PlaceResponseDto) => {
    setSelectedPlace(place);
    setModalVisible(true);
  };

  const handleEdit = (place: PlaceResponseDto) => {
    setSelectedPlace(place);
    setFormAction('edit');
    setFormVisible(true);
  };

  const handleCreate = () => {
    setSelectedPlace(null);
    setFormAction('create');
    setFormVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await deletePlace(id);
      message.success('Place deleted successfully');
      fetchPlaces();
    } catch (error) {
      message.error('Failed to delete place');
    }
  };

  const columns = [
    /*{
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },*/
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Action',
      key: 'action',
      render: (_: any, record: PlaceResponseDto) => (
        <Space size="middle">
          <Button onClick={() => handleView(record)}>View</Button>
          <Button type="primary" onClick={() => handleEdit(record)}>
            Edit
          </Button>
          <Popconfirm
            title="Are you sure to delete this place?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button danger>Delete</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <Button type="primary" onClick={handleCreate}>
          Add Place
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={places}
        rowKey="id"
        loading={loading}
      />
      <PlaceModal
        place={selectedPlace}
        visible={modalVisible}
        onClose={() => setModalVisible(false)}
        onEdit={(place) => {
          setModalVisible(false);
          setSelectedPlace(place);
          setFormAction('edit');
          setFormVisible(true);
        }}
      />
      <PlaceForm
        visible={formVisible}
        onCancel={() => setFormVisible(false)}
        onSuccess={() => {
          setFormVisible(false);
          fetchPlaces();
        }}
        action={formAction}
        place={selectedPlace}
      />
    </div>
  );
};

export default PlacesTable;