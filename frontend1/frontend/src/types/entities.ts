export interface PlaceResponseDto {
    id: number;
    name: string;
    address: string;
    description: string;
  }
  
  export interface PlaceRequestDto {
    name: string;
    address: string;
    description: string;
  }
  
  export interface RouteResponseDto {
    id: number;
    name: string;
    description: string;
    authorId: number;
    places: PlaceResponseDto[];
  }
  
  export interface RouteRequestDto {
    name: string;
    description: string;
    authorId: number;
  }
  
  export interface UserResponseDto {
    id: number;
    name: string;
    email: string;
    routes: RouteResponseDto[];
  }
  
  export interface UserRequestDto {
    name: string;
    email: string;
  }