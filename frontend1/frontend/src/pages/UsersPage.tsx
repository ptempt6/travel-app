import React from 'react';
import UsersTable from '../components/TableView/UsersTable';
import MainLayout from '../components/MainLayout/MainLayout';

const UsersPage: React.FC = () => {
  return (
    <MainLayout>
      <UsersTable />
    </MainLayout>
  );
};

export default UsersPage;