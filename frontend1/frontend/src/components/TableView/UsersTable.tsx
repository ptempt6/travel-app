import React, { useState, useEffect } from 'react';
import { Table, Button, Space,  message, Popconfirm, Tag } from 'antd';
import { UserResponseDto } from '../../types/entities';
import { getAllUsers, deleteUser } from '../../api/userApi';
import UserModal from '../EntityModal/UserModal';
import UserForm from './UserForm';

const UsersTable: React.FC = () => {
  const [users, setUsers] = useState<UserResponseDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserResponseDto | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [formAction, setFormAction] = useState<'create' | 'edit'>('create');

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const data = await getAllUsers();
      setUsers(data);
    } catch (error) {
      message.error('Failed to fetch users');
    } finally {
      setLoading(false);
    }
  };

  const renderRoutes = (routes: any[]) => {
    if (routes.length === 0) return 'No routes';
    
    const visibleRoutes = routes.slice(0, 2);
    const hasMore = routes.length > 2;
    
    return (
      <Space size={[0, 8]} wrap>
        {visibleRoutes.map(route => (
          <Tag key={route.id}>{route.name}</Tag>
        ))}
        {hasMore && <Tag>...</Tag>}
      </Space>
    );
  };

  const handleView = (user: UserResponseDto) => {
    setSelectedUser(user);
    setModalVisible(true);
  };

  const handleEdit = (user: UserResponseDto) => {
    setSelectedUser(user);
    setFormAction('edit');
    setFormVisible(true);
  };

  const handleCreate = () => {
    setSelectedUser(null);
    setFormAction('create');
    setFormVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteUser(id);
      message.success('User deleted successfully');
      fetchUsers();
    } catch (error) {
      message.error('Failed to delete user');
    }
  };

  const columns = [
  /*  {
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
      title: 'Routes',
      dataIndex: 'routes',
      key: 'routes',
      render: renderRoutes,
    },
    {
      title: 'Action',
      key: 'action',
      render: (_: any, record: UserResponseDto) => (
        <Space size="middle">
          <Button onClick={() => handleView(record)}>View</Button>
          <Button type="primary" onClick={() => handleEdit(record)}>
            Edit
          </Button>
          <Popconfirm
            title="Are you sure to delete this user?"
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
          Add User
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={users}
        rowKey="id"
        loading={loading}
      />
      <UserModal
        user={selectedUser}
        visible={modalVisible}
        onClose={() => setModalVisible(false)}
        onEdit={(user) => {
          setModalVisible(false);
          setSelectedUser(user);
          setFormAction('edit');
          setFormVisible(true);
        }}
        onRouteClick={() => {
          // Implement route click handling
        }}
      />
      <UserForm
        visible={formVisible}
        onCancel={() => setFormVisible(false)}
        onSuccess={() => {
          setFormVisible(false);
          fetchUsers();
        }}
        action={formAction}
        user={selectedUser}
      />
    </div>
  );
};

export default UsersTable;