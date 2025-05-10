import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Modal, message } from 'antd';
import { PlaceRequestDto, PlaceResponseDto } from '../../types/entities';
import { createPlace, updatePlace } from '../../api/placeApi';

interface PlaceFormProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
  action: 'create' | 'edit';
  place: PlaceResponseDto | null;
}

const PlaceForm: React.FC<PlaceFormProps> = ({ 
  visible, 
  onCancel, 
  onSuccess,
  action,
  place 
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible && place && action === 'edit') {
      form.setFieldsValue({
        name: place.name,
        address: place.address,
        description: place.description,
      });
    } else {
      form.resetFields();
    }
  }, [visible, place, action, form]);

  const handleSubmit = async (values: PlaceRequestDto) => {
    setLoading(true);
    try {
      if (action === 'create') {
        await createPlace(values);
        message.success('Place created successfully');
      } else if (place) {
        await updatePlace(place.id, values);
        message.success('Place updated successfully');
      }
      onSuccess();
    } catch (error) {
      message.error(`Failed to ${action === 'create' ? 'create' : 'update'} place`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      visible={visible}
      title={action === 'create' ? 'Create Place' : 'Edit Place'}
      onCancel={onCancel}
      footer={null}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSubmit}
      >
        <Form.Item
          name="name"
          label="Name"
          rules={[{ required: true, message: 'Please input the place name!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="address"
          label="Address"
          rules={[{ required: true, message: 'Please input the place address!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="description"
          label="Description"
          rules={[{ required: true, message: 'Please input the place description!' }]}
        >
          <Input.TextArea rows={4} />
        </Form.Item>
        <Form.Item>
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

export default PlaceForm;