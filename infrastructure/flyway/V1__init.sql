CREATE TABLE player (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  team VARCHAR(255),
  position VARCHAR(50),
  external_id VARCHAR(100),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE player_price (
  id BIGSERIAL PRIMARY KEY,
  player_id BIGINT NOT NULL REFERENCES player(id) ON DELETE CASCADE,
  price NUMERIC(12,2) NOT NULL,
  trend VARCHAR(32),
  captured_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE tracked_player (
  id BIGSERIAL PRIMARY KEY,
  player_id BIGINT NOT NULL REFERENCES player(id) ON DELETE CASCADE,
  clause NUMERIC(12,2),
  clause_release_date DATE,
  notes TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE INDEX idx_player_price_player ON player_price(player_id);
CREATE INDEX idx_tracked_player_player ON tracked_player(player_id);
