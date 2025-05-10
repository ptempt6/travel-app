import React from 'react';
import { Layout, Menu, theme, Typography } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import './../../styles/global.css';

const { Header, Content, Footer } = Layout;
const { Title } = Typography;

const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { token: { colorBgContainer } } = theme.useToken();
  const location = useLocation();

  return (
    <Layout style={{ 
      minHeight: '100vh',
      width: '100%',
      maxWidth: '100vw',
      overflowX: 'hidden'
    }}>
      <Header style={{
        position: 'fixed',
        zIndex: 1000,
        width: '100%',
        padding: '0 24px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between'
      }}>
        <Link to="/routes" style={{ display: 'flex', alignItems: 'center' }}>
          <Title level={4} style={{ 
            color: 'white',
            margin: 0,
            marginRight: 24,
            cursor: 'pointer'
          }}>
            Travel App
          </Title>
        </Link>
        
        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[location.pathname]}
          style={{ 
            flex: 1,
            minWidth: 0,
            lineHeight: '64px'
          }}
        >
          <Menu.Item key="/users"><Link to="/users">Users</Link></Menu.Item>
          <Menu.Item key="/routes"><Link to="/routes">Routes</Link></Menu.Item>
          <Menu.Item key="/places"><Link to="/places">Places</Link></Menu.Item>
        </Menu>
      </Header>

      <Content style={{
        marginTop: 64,
        padding: '24px 5%',
        width: '100%',
        maxWidth: '100%',
        flex: 1
      }}>
        <div style={{
          background: colorBgContainer,
          padding: 24,
          minHeight: 'calc(100vh - 64px - 70px)',
          borderRadius: 8,
          width: '100%',
          margin: '0 auto'
        }}>
          {children}
        </div>
      </Content>

      <Footer style={{
        textAlign: 'center',
        padding: '16px 50px',
        width: '100%'
      }}>
        Travel App ©{new Date().getFullYear()}
      </Footer>
    </Layout>
  );
};

export default MainLayout;