import React from 'react';
import { Modal, Descriptions, Button, List, Avatar } from 'antd';
import { UserResponseDto } from '../../types/entities';

import { useNavigate } from 'react-router-dom';



// В кнопке для маршрутов:
interface UserModalProps {
  user: UserResponseDto | null;
  visible: boolean;
  onClose: () => void;
  onEdit: (user: UserResponseDto) => void;
  onRouteClick: (routeId: number) => void;
}

const UserModal: React.FC<UserModalProps> = ({ 
  user, 
  visible, 
  onClose, 
  onEdit 
}) => {
  if (!user) return null;
  // В компоненте:
const navigate = useNavigate();

  return (
    <Modal
      title={user.name}
      open={visible}
      onCancel={onClose}
      footer={[
        <Button key="edit" type="primary" onClick={() => onEdit(user)}>
          Edit
        </Button>,
        <Button key="close" onClick={onClose}>
          Close
        </Button>,
      ]}
      width={800}
    >
      <Descriptions bordered column={1}>
        <Descriptions.Item label="Email">{user.email}</Descriptions.Item>
        <Descriptions.Item label="Routes">
          <List
            itemLayout="horizontal"
            dataSource={user.routes}
            renderItem={route => (
              <List.Item>
                <List.Item.Meta
                  avatar={<Avatar src="https://joesch.moe/api/v1/random" />}
                  title={

                    <Button 
                      type="link" 
                      onClick={() => {
                        onClose();
                        navigate(`/routes/${route.id}`);
                      }}
                    >
                      {route.name}
                    </Button>
                  }
                  description={route.description}
                />
              </List.Item>
            )}
          />
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default UserModal;