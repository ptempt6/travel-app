import axios from 'axios';
import { UserResponseDto, UserRequestDto} from '../types/entities';

const API_BASE_URL = 'http://localhost:8081/api/users';

export const getAllUsers = async (): Promise<UserResponseDto[]> => {
  const response = await axios.get(API_BASE_URL);
  return response.data;
};

export const getUserById = async (id: number): Promise<UserResponseDto> => {
  const response = await axios.get(`${API_BASE_URL}/${id}`);
  return response.data;
};

export const createUser = async (dto: UserRequestDto): Promise<UserResponseDto> => {
  const response = await axios.post(API_BASE_URL, dto);
  return response.data;
};

export const updateUser = async (id: number, dto: UserRequestDto): Promise<UserResponseDto> => {
  const response = await axios.put(`${API_BASE_URL}/${id}`, dto);
  return response.data;
};

export const deleteUser = async (id: number): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/${id}`);
};