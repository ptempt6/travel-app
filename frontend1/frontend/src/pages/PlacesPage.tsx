import React from 'react';
import PlacesTable from '../components/TableView/PlacesTable';
import MainLayout from '../components/MainLayout/MainLayout';

const PlacesPage: React.FC = () => {
  return (
    <MainLayout>
      <PlacesTable />
    </MainLayout>
  );
};

export default PlacesPage;