import React, { useEffect, useState } from 'react';
import { Modal, Descriptions, Button, message } from 'antd';
import { PlaceResponseDto } from '../../types/entities';
import { getRoutesByPlace } from '../../api/placeApi';
import { useNavigate } from 'react-router-dom';



// В кнопке для маршрутов:

interface PlaceModalProps {
  place: PlaceResponseDto | null;
  visible: boolean;
  onClose: () => void;
  onEdit: (place: PlaceResponseDto) => void;
}

const PlaceModal: React.FC<PlaceModalProps> = ({ place, visible, onClose, onEdit }) => {
  const [routes, setRoutes] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  // В компоненте:
const navigate = useNavigate();

  useEffect(() => {
    if (place && visible) {
      setLoading(true);
      getRoutesByPlace(place.id)
        .then(data => {
          setRoutes(data);
        })
        .catch(() => {
          message.error('Failed to load routes for this place');
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [place, visible]);

  if (!place) return null;

  return (
    <Modal
      title={place.name}
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="edit" type="primary" onClick={() => onEdit(place)}>
          Edit
        </Button>,
        <Button key="close" onClick={onClose}>
          Close
        </Button>,
      ]}
      width={800}
    >
      <Descriptions bordered column={1}>
        <Descriptions.Item label="Address">{place.address}</Descriptions.Item>
        <Descriptions.Item label="Description">{place.description}</Descriptions.Item>
        <Descriptions.Item label="Routes">
          {loading ? (
            'Loading...'
          ) : routes.length > 0 ? (
            <ul>
              {routes.map(route => (
                <li key={route.id}>

<Button 
  type="link" 
  onClick={() => {
    onClose();
    navigate(`/routes/${route.id}`);
  }}
>
  {route.name}
</Button>
                </li>
              ))}
            </ul>
          ) : (
            'No routes include this place'
          )}
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default PlaceModal;