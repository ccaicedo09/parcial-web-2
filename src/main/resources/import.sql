INSERT INTO roles(name) VALUES('RECTOR') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('DOCENTE') ON CONFLICT DO NOTHING;
INSERT INTO roles(name) VALUES('ESTUDIANTE') ON CONFLICT DO NOTHING;