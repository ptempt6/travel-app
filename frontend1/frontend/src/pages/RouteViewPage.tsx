import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {  message, Spin } from 'antd';
import RouteModal from '../components/EntityModal/RouteModal';
import { getRouteById } from '../api/routeApi';
//import { getUserById } from '../api/userApi';
import { RouteResponseDto } from '../types/entities';
import RouteForm from '../components/TableView/RouteForm';
import MainLayout from '../components/MainLayout/MainLayout';

export const RouteViewPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [route, setRoute] = useState<RouteResponseDto | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  //const [authorName, setAuthorName] = useState('');

  useEffect(() => {
    if (id) {
      fetchRoute();
    }
  }, [id]);

  const fetchRoute = async () => {
    setLoading(true);
    try {
      const data = await getRouteById(Number(id));
      setRoute(data);
      if (data.authorId) {
        fetchAuthorName(data.authorId);
      }
    } catch (error) {
      message.error('Failed to load route');
      navigate('/routes');
    } finally {
      setLoading(false);
    }
  };

  const fetchAuthorName = async (userId: number) => {
    try {

      userId++;
      userId--;
      //const user = await getUserById(userId);
      //setAuthorName(user.name);
    } catch (error) {
      //setAuthorName(`User #${userId}`);
    }
  };

  const handleEdit = () => {
    setFormVisible(true);
  };

  const handleClose = () => {
    navigate('/routes');
  };

  const handlePlaceClick = (placeId: number) => {
    navigate(`/places/${placeId}`);
  };

  const handleFormSuccess = () => {
    setFormVisible(false);
    fetchRoute();
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
        ) : route ? (
          <>
            <RouteModal 
              route={route} 
              visible={true}
              onClose={handleClose}
              onEdit={handleEdit}
              onPlaceClick={handlePlaceClick}
            />
            <RouteForm
              visible={formVisible}
              onCancel={() => setFormVisible(false)}
              onSuccess={handleFormSuccess}
              action="edit"
              route={route}
            />
          </>
        ) : (
          <div>Route not found</div>
        )}
      </div>
    </MainLayout>
  );
};