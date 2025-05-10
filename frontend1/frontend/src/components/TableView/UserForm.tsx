import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Modal, message } from 'antd';
import { UserRequestDto, UserResponseDto } from '../../types/entities';
import { createUser, updateUser } from '../../api/userApi';

interface UserFormProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
  action: 'create' | 'edit';
  user: UserResponseDto | null;
}

const UserForm: React.FC<UserFormProps> = ({ 
  visible, 
  onCancel, 
  onSuccess,
  action,
  user 
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible && user && action === 'edit') {
      form.setFieldsValue({
        name: user.name,
        email: user.email,
      });
    } else {
      form.resetFields();
    }
  }, [visible, user, action, form]);

  const handleSubmit = async (values: UserRequestDto) => {
    setLoading(true);
    try {
      if (action === 'create') {
        await createUser(values);
        message.success('User created successfully');
      } else if (user) {
        await updateUser(user.id, values);
        message.success('User updated successfully');
      }
      onSuccess();
    } catch (error) {
      message.error(`Failed to ${action === 'create' ? 'create' : 'update'} user`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      visible={visible}
      title={action === 'create' ? 'Create User' : 'Edit User'}
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
          rules={[{ required: true, message: 'Please input the user name!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="email"
          label="Email"
          rules={[
            { required: true, message: 'Please input the user email!' },
            { type: 'email', message: 'Please input a valid email!' }
          ]}
        >
          <Input />
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

export default UserForm;