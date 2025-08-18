-- Add participant_count column to rooms table
ALTER TABLE rooms ADD COLUMN participant_count INTEGER NOT NULL DEFAULT 1;

-- Update existing rooms to have participant_count = 1 (assuming creator is still there)
UPDATE rooms SET participant_count = 1 WHERE participant_count IS NULL;