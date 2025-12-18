INSERT INTO employees (email, password, first_name, last_name, phone_number, department_id, role)
VALUES
('john@abv.bg', '$2a$10$IjlQ1Z5oIBQVABhDL2h.6uzmKOGj.s70pVuJ2AtF/k1An5MRz.A5.', 'John', 'Doe', '0889124123', NULL, 'USER'),
('jane@abv.bg', '$2a$10$X2kxEqnSJrX0DH2dLIREJe/znGFftja0tDS7XyDpeHyTA5M5mS0Da', 'Jane', 'Smith', '08875123', 1, 'USER'),
('alice@abv.bg', '$2a$10$IzrpiQXkdUxi309NDWT2DOOEWQNJfpWH9sNIz27X9kybjKINFwfmK', 'Alice', 'Johnson', '0871234123', 2, 'ADMIN'),
('bob@abv.bg', '$2a$10$9Ag.DXv.k8aIOrrs6N8pRei1bKH.w0.5UsQRTKRl5s6jCH7DmqEQm', 'Bob', 'Brown', '0123000000', 3, 'ADMIN');