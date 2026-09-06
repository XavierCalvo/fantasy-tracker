CREATE TABLE player (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  team VARCHAR(255),
  position VARCHAR(50),
  external_id VARCHAR(100),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT uk_player_external_id UNIQUE (external_id)
);

CREATE TABLE player_price (
  id BIGSERIAL PRIMARY KEY,
  player_id BIGINT NOT NULL REFERENCES player(id) ON DELETE CASCADE,
  price NUMERIC(15,0) NOT NULL,
  trend_amount BIGINT,
  trend_type VARCHAR(32),
  captured_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT ck_trend_type CHECK (trend_type IN (
    'INFLECTION_POSITIVE',
    'ACCELERATING_STRONGLY_UP',
    'ACCELERATING_UP',
    'STABLE_UP',
    'DECELERATING_UP',
    'DECELERATING_STRONGLY_UP',
    'INFLECTION_NEGATIVE',
    'DECELERATING_STRONGLY_DOWN',
    'DECELERATING_DOWN',
    'STABLE_DOWN',
    'ACCELERATING_DOWN',
    'ACCELERATING_STRONGLY_DOWN'
  ))
);

CREATE TABLE tracked_player (
  id BIGSERIAL PRIMARY KEY,
  player_id BIGINT NOT NULL REFERENCES player(id) ON DELETE CASCADE,
  status VARCHAR(32) NOT NULL DEFAULT 'WATCHING',
  clause NUMERIC(15,0),
  clause_release_date DATE,
  notes TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT ck_tracked_player_status CHECK (status IN ('WATCHING', 'OWNED', 'DISCARDED'))
);

CREATE INDEX idx_player_price_player ON player_price(player_id);
CREATE INDEX idx_tracked_player_player ON tracked_player(player_id);
CREATE INDEX idx_tracked_player_status ON tracked_player(status);
