import axios from 'axios';
import { RouteResponseDto, RouteRequestDto } from '../types/entities';

const API_BASE_URL = 'http://localhost:8081/api/routes';

export const getAllRoutes = async (): Promise<RouteResponseDto[]> => {
  const response = await axios.get(API_BASE_URL);
  return response.data;
};

export const getMoreThanRoutes = async (minPlaces: number): Promise<RouteResponseDto[]> => {
  const response = await axios.get(`${API_BASE_URL}/more-than?minPlaces=${minPlaces}`);
  return response.data;
};

export const getRouteById = async (id: number): Promise<RouteResponseDto> => {
  const response = await axios.get(`${API_BASE_URL}/${id}`);
  return response.data;
};

export const createRoute = async (dto: RouteRequestDto): Promise<RouteResponseDto> => {
  const response = await axios.post(API_BASE_URL, dto);
  return response.data;
};

export const updateRoute = async (id: number, dto: RouteRequestDto): Promise<RouteResponseDto> => {
  const response = await axios.put(`${API_BASE_URL}/${id}`, dto);
  return response.data;
};

export const deleteRoute = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/${id}`);
};

export const addPlaceToRoute = async (routeId: number, placeId: number): Promise<void> => {
  await axios.post(`${API_BASE_URL}/${routeId}/add/${placeId}`);
};

export const removePlaceFromRoute = async (routeId: number, placeId: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/${routeId}/remove/${placeId}`);
};