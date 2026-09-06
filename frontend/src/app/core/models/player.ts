export interface Player {
  id: number;
  name: string;
  team: string;
  position: string;
  externalId: string | null;
  createdAt: string;
}

export interface PlayerRequest {
  name: string;
  team: string;
  position: string;
  externalId?: string | null;
}
