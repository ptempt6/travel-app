import React, { useState, useEffect } from 'react';
import { Table, Button, Space, message, Popconfirm, Tag } from 'antd';
import { RouteResponseDto } from '../../types/entities';
import { getAllRoutes, deleteRoute } from '../../api/routeApi';
import { getUserById } from '../../api/userApi';
import RouteModal from '../EntityModal/RouteModal';
import RouteForm from './RouteForm';

interface RouteWithAuthor extends RouteResponseDto {
  authorName?: string;
}

const RoutesTable: React.FC = () => {
  const [routes, setRoutes] = useState<RouteWithAuthor[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedRoute, setSelectedRoute] = useState<RouteResponseDto | null>(null);
  const [formVisible, setFormVisible] = useState(false);
  const [formAction, setFormAction] = useState<'create' | 'edit'>('create');

  useEffect(() => {
    fetchRoutes();
  }, []);

  const fetchRoutes = async () => {
    setLoading(true);
    try {
      const routesData = await getAllRoutes();
      
      // Получаем имена авторов для каждого маршрута
      const routesWithAuthors = await Promise.all(
        routesData.map(async (route) => {
          try {
            const author = await getUserById(route.authorId);
            return {
              ...route,
              authorName: author.name
            };
          } catch (error) {
            console.error(`Failed to fetch author for route ${route.id}`);
            return {
              ...route,
              authorName: `User #${route.authorId}`
            };
          }
        })
      );
      
      setRoutes(routesWithAuthors);
    } catch (error) {
      message.error('Failed to fetch routes');
    } finally {
      setLoading(false);
    }
  };

  const handleView = (route: RouteResponseDto) => {
    setSelectedRoute(route);
    setModalVisible(true);
  };

  const handleEdit = (route: RouteResponseDto) => {
    setSelectedRoute(route);
    setFormAction('edit');
    setFormVisible(true);
  };

  const handleCreate = () => {
    setSelectedRoute(null);
    setFormAction('create');
    setFormVisible(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteRoute(id);
      message.success('Route deleted successfully');
      fetchRoutes();
    } catch (error) {
      message.error('Failed to delete route');
    }
  };

  const renderPlaces = (places: any[]) => {
    if (places.length === 0) return 'No places';
    
    const visiblePlaces = places.slice(0, 2);
    const hasMore = places.length > 2;
    
    return (
      <Space size={[0, 8]} wrap>
        {visiblePlaces.map(place => (
          <Tag key={place.id}>{place.name}</Tag>
        ))}
        {hasMore && <Tag>...</Tag>}
      </Space>
    );
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Author',
      dataIndex: 'authorName',
      key: 'authorName',
    },
    {
      title: 'Places',
      dataIndex: 'places',
      key: 'places',
      render: renderPlaces,
    },
    {
      title: 'Action',
      key: 'action',
      render: (_: any, record: RouteResponseDto) => (
        <Space size="middle">
          <Button onClick={() => handleView(record)}>View</Button>
          <Button type="primary" onClick={() => handleEdit(record)}>
            Edit
          </Button>
          <Popconfirm
            title="Are you sure to delete this route?"
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
          Add Route
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={routes}
        rowKey="id"
        loading={loading}
      />
      <RouteModal
        route={selectedRoute}
        visible={modalVisible}
        onClose={() => setModalVisible(false)}
        onEdit={(route) => {
          setModalVisible(false);
          setSelectedRoute(route);
          setFormAction('edit');
          setFormVisible(true);
        }}
        onPlaceClick={() => {
          // Implement place click handling
        }}
      />
      <RouteForm
        visible={formVisible}
        onCancel={() => setFormVisible(false)}
        onSuccess={() => {
          setFormVisible(false);
          fetchRoutes();
        }}
        action={formAction}
        route={selectedRoute}
      />
    </div>
  );
};

export default RoutesTable;