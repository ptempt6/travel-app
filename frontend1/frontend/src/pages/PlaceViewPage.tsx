import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {  message, Spin } from 'antd';
import PlaceModal from '../components/EntityModal/PlaceModal';
import { getPlaceById } from '../api/placeApi';
import { PlaceResponseDto } from '../types/entities';
import PlaceForm from '../components/TableView/PlaceForm';
import MainLayout from '../components/MainLayout/MainLayout';

export const PlaceViewPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [place, setPlace] = useState<PlaceResponseDto | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (id) {
      fetchPlace();
    }
  }, [id]);

  const fetchPlace = async () => {
    setLoading(true);
    try {
      const data = await getPlaceById(Number(id));
      setPlace(data);
    } catch (error) {
      message.error('Failed to load place');
      navigate('/places');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    setFormVisible(true);
  };

  const handleClose = () => {
    navigate('/places');
  };

  const handleFormSuccess = () => {
    setFormVisible(false);
    fetchPlace();
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
        ) : place ? (
          <>
            <PlaceModal 
              place={place} 
              visible={true}
              onClose={handleClose}
              onEdit={handleEdit}
            />
            <PlaceForm
              visible={formVisible}
              onCancel={() => setFormVisible(false)}
              onSuccess={handleFormSuccess}
              action="edit"
              place={place}
            />
          </>
        ) : (
          <div>Place not found</div>
        )}
      </div>
    </MainLayout>
  );
};