import React from 'react';
import { Card, Col, Row, Typography } from 'antd';
import { Link } from 'react-router-dom';

const { Title } = Typography;

const HomePage: React.FC = () => {
  return (
    <div style={{ 
      maxWidth: '2000px',
      margin: '0 auto',
      padding: '0 24px'
    }}>
     
    
      <Title level={2} style={{ textAlign: 'center', marginBottom: '2rem' }}>
        Welcome to Travel App
      </Title>
      <Row gutter={16}>
        <Col span={8}>
          <Link to="/users">
            <Card title="Users" bordered={false} hoverable>
              Manage application users and their routes
            </Card>
          </Link>
        </Col>
        <Col span={8}>
          <Link to="/routes">
            <Card title="Routes" bordered={false} hoverable>
              Explore and manage travel routes
            </Card>
          </Link>
        </Col>
        <Col span={8}>
          <Link to="/places">
            <Card title="Places" bordered={false} hoverable>
              Discover and manage interesting places
            </Card>
          </Link>
        </Col>
      </Row>
    </div>
  );
};

export default HomePage;