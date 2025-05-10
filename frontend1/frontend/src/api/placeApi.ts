import axios from 'axios';
import { PlaceResponseDto, PlaceRequestDto, RouteResponseDto } from '../types/entities';

const API_BASE_URL = 'http://localhost:8081/api/places';

export const getAllPlaces = async (): Promise<PlaceResponseDto[]> => {
  const response = await axios.get(API_BASE_URL);
  return response.data;
};

export const getNotVisitedPlaces = async (userId: number): Promise<PlaceResponseDto[]> => {
  const response = await axios.get(`${API_BASE_URL}/not-visited?userId=${userId}`);
  return response.data;
};

export const getPlaceById = async (id: number): Promise<PlaceResponseDto> => {
  const response = await axios.get(`${API_BASE_URL}/${id}`);
  return response.data;
};

export const getRoutesByPlace = async (id: number): Promise<RouteResponseDto[]> => {
  const response = await axios.get(`${API_BASE_URL}/${id}/routes`);
  return response.data;
};

export const createPlace = async (dto: PlaceRequestDto): Promise<PlaceResponseDto> => {
  const response = await axios.post(API_BASE_URL, dto);
  return response.data;
};

export const createPlaces = async (dtos: PlaceRequestDto[]): Promise<PlaceResponseDto[]> => {
  const response = await axios.post(`${API_BASE_URL}/bulk`, dtos);
  return response.data;
};

export const updatePlace = async (id: number, dto: PlaceRequestDto): Promise<PlaceResponseDto> => {
  const response = await axios.put(`${API_BASE_URL}/${id}`, dto);
  return response.data;
};

export const deletePlace = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/${id}`);
};