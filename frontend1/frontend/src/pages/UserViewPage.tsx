import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {  message, Spin } from 'antd';
import UserModal from '../components/EntityModal/UserModal';
import { getUserById } from '../api/userApi';
import { UserResponseDto } from '../types/entities';
import UserForm from '../components/TableView/UserForm';
import MainLayout from '../components/MainLayout/MainLayout';

export const UserViewPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [user, setUser] = useState<UserResponseDto | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) {
      fetchUser();
    }
  }, [id]);

  const fetchUser = async () => {
    setLoading(true);
    try {
      const data = await getUserById(Number(id));
      setUser(data);
    } catch (error) {
      message.error('Failed to load user');
      navigate('/users');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    setFormVisible(true);
  };

  const handleClose = () => {
    navigate('/users');
  };

  const handleRouteClick = (routeId: number) => {
    navigate(`/routes/${routeId}`);
  };

  const handleFormSuccess = () => {
    setFormVisible(false);
    fetchUser(); // Refresh user data after edit
  };

  return (
    <MainLayout>
      <div style={{ 
        background: '#fff', 
        padding: 24, 
        minHeight: 'calc(100vh - 64px - 70px)',
        borderRadius: 8 
      }}>
        {loading ? (
          <div style={{ display: 'flex', justifyContent: 'center', padding: '50px 0' }}>
            <Spin size="large" />
          </div>
        ) : user ? (
          <>
            <UserModal 
              user={user} 
              visible={true}
              onClose={handleClose}
              onEdit={handleEdit}
              onRouteClick={handleRouteClick}
            />
            <UserForm
              visible={formVisible}
              onCancel={() => setFormVisible(false)}
              onSuccess={handleFormSuccess}
              action="edit"
              user={user}
            />
          </>
        ) : (
          <div>User not found</div>
        )}
      </div>
    </MainLayout>
  );
};