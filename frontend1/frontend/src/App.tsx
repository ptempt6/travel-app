import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import UsersPage from './pages/UsersPage';
import RoutesPage from './pages/RoutesPage';
import PlacesPage from './pages/PlacesPage';
import {UserViewPage} from './pages/UserViewPage';
import {RouteViewPage} from './pages/RouteViewPage';
import {PlaceViewPage} from './pages/PlaceViewPage';

const App: React.FC = () => {
  return (
    <Router>
<Routes>
  <Route path="/" element={<RoutesPage />} />
  <Route path="/users" element={<UsersPage />} />
  <Route path="/users/:id" element={<UserViewPage />} />
  <Route path="/routes" element={<RoutesPage />} />
  <Route path="/routes/:id" element={<RouteViewPage />} />
  <Route path="/places" element={<PlacesPage />} />
  <Route path="/places/:id" element={<PlaceViewPage />} />
</Routes>
    </Router>
  );
};

export default App;