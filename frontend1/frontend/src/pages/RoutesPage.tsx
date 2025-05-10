import React from 'react';
import RoutesTable from '../components/TableView/RoutesTable';
import MainLayout from '../components/MainLayout/MainLayout';

const RoutesPage: React.FC = () => {
  return (
    <MainLayout>
      <RoutesTable />
    </MainLayout>
  );
};

export default RoutesPage;