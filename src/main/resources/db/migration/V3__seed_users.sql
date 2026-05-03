-- ============================================================
-- Workshop Management System — Seed: Users
-- Senha de todos os usuários: workshop123
-- ============================================================

INSERT INTO users (id, email, password_hash, role, active) VALUES
('c3000000-0000-0000-0000-000000000001',
 'admin@workshop.com',
 '$2a$10$63Uk3ObeqJAecBiIWduDC.RHJ3c3snYb77yCHgUCN5FSy9uK/Upku',
 'ROLE_ADMIN', TRUE),

('c3000000-0000-0000-0000-000000000002',
 'consultor@workshop.com',
 '$2a$10$7ybKyCHLUWhaRkdXn52E8uaMjT9.QLm1X000J4ruBHXsnQH.Ss04W',
 'ROLE_CONSULTANT', TRUE),

('c3000000-0000-0000-0000-000000000003',
 'mecanico@workshop.com',
 '$2a$10$1ror3LCbQCcu0.J8mC0yOunJw4I81D8iE5u4md1D5obdO63jDrmlG',
 'ROLE_MECHANIC', TRUE),

('c3000000-0000-0000-0000-000000000004',
 'estoquista@workshop.com',
 '$2a$10$En8KZREB74AZuBFpoYzsAeZRo0ioAEIHDIXMUL6/lPpH3x9m1g3Tm',
 'ROLE_STOCKIST', TRUE);
