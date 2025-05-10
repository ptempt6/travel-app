import React, { useState, useEffect } from 'react';
import { Modal, Descriptions, Button, List, Avatar, message } from 'antd';
import { RouteResponseDto } from '../../types/entities';
import { useNavigate } from 'react-router-dom';
import { getUserById } from '../../api/userApi'; // Добавьте этот импорт

interface RouteModalProps {
  route: RouteResponseDto | null;
  visible: boolean;
  onClose: () => void;
  onEdit: (route: RouteResponseDto) => void;
  onPlaceClick: (placeId: number) => void;
}

const RouteModal: React.FC<RouteModalProps> = ({ 
  route, 
  visible, 
  onClose, 
  onEdit
}) => {
  const navigate = useNavigate();
  const [authorName, setAuthorName] = useState<string>('Loading...');
  const [loadingAuthor, setLoadingAuthor] = useState(false);

  useEffect(() => {
    if (route && visible) {
      setLoadingAuthor(true);
      getUserById(route.authorId)
        .then(user => {
          setAuthorName(user.name);
        })
        .catch(() => {
          message.error('Failed to load author info');
          setAuthorName(`User #${route.authorId}`);
        })
        .finally(() => {
          setLoadingAuthor(false);
        });
    }
  }, [route, visible]);

  if (!route) return null;

  return (
    <Modal
      title={route.name}
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="edit" type="primary" onClick={() => onEdit(route)}>
          Edit
        </Button>,
        <Button key="close" onClick={onClose}>
          Close
        </Button>,
      ]}
      width={800}
    >
      <Descriptions bordered column={1}>
        <Descriptions.Item label="Description">{route.description}</Descriptions.Item>
        <Descriptions.Item label="Author">
          {loadingAuthor ? (
            'Loading...'
          ) : (
            <Button 
              type="link" 
              onClick={() => {
                onClose();
                navigate(`/users/${route.authorId}`);
              }}
            >
              {authorName}
            </Button>
          )}
        </Descriptions.Item>
        <Descriptions.Item label="Places">
          <List
            itemLayout="horizontal"
            dataSource={route.places}
            renderItem={place => (
              <List.Item>
                <List.Item.Meta
                  avatar={<Avatar src="https://joesch.moe/api/v1/random" />}
                  title={
                    <Button 
                      type="link" 
                      onClick={() => {
                        onClose();
                        navigate(`/places/${place.id}`);
                      }}
                    >
                      {place.name}
                    </Button>
                  }
                  description={place.address}
                />
              </List.Item>
            )}
          />
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default RouteModal;