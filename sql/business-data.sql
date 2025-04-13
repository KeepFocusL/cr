-- 车站表
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906233866197274624, '福州', 'fuzhou', 'FZ', '2025-03-30 14:35:44.000', '2025-04-13 09:24:27.534');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906233923759902720, '福州南', 'fuzhounan', 'FZN', '2025-03-30 14:35:58.000', '2025-04-13 09:24:23.570');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906234279193612288, '厦门北', 'xiamenbei', 'XMB', '2025-03-30 14:37:22.000', '2025-04-13 09:24:07.921');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906234306150404096, '厦门', 'xiamen', 'XM', '2025-03-30 14:37:29.000', '2025-04-13 09:24:17.490');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228787740250112, '北京', 'beijing', 'BJ', '2025-04-13 09:23:46.567', '2025-04-13 09:23:46.567');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228846938656768, '南京', 'nanjing', 'NJ', '2025-04-13 09:24:00.682', '2025-04-13 09:24:00.682');



-- 车次
INSERT INTO business.train (id, code, type, start, start_pinyin, start_time, end, end_pinyin, end_time, created_at, updated_at) VALUES (1911229332823609344, 'G5114', 'G', '厦门', 'xiamen', '14:33:00', '福州', 'fuzhou', '16:09:00', '2025-04-13 09:25:56.526', '2025-04-13 09:25:56.526');


-- 火车车站
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230056378798080, 'G5114', 1, '厦门', 'xiamen', '14:33:00', '14:33:00', '00:00:00', 0.00, '2025-04-13 09:28:49.000', '2025-04-13 09:28:55.057');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230307407892480, 'G5114', 2, '厦门北', 'xiamenbei', '14:55:00', '14:57:00', '00:02:00', 10.00, '2025-04-13 09:29:48.885', '2025-04-13 09:29:48.885');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230631510151168, 'G5114', 3, '福州南', 'fuzhounan', '15:52:00', '15:54:00', '00:02:00', 125.00, '2025-04-13 09:31:06.000', '2025-04-13 09:32:05.987');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230780772847616, 'G5114', 4, '福州', 'fuzhou', '16:09:00', '16:09:00', '00:00:00', 38.00, '2025-04-13 09:31:41.744', '2025-04-13 09:31:41.744');


-- 火车车厢（不完整）
INSERT INTO business.train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1906238270711402496, 'G5114', 1, '2', 15, 3, 5, '2025-03-30 14:53:14.000', '2025-04-13 09:35:46.091');
INSERT INTO business.train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1906238645929644032, 'G5114', 2, '1', 12, 3, 4, '2025-03-30 14:54:44.000', '2025-04-13 09:35:41.645');


-- 火车座位（不完整）
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231867982581760, 'G5114', 1, '01', 'A', '2', 1, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868003553280, 'G5114', 1, '01', 'B', '2', 2, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868024524800, 'G5114', 1, '01', 'C', '2', 3, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868045496320, 'G5114', 1, '01', 'D', '2', 4, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868062273536, 'G5114', 1, '01', 'F', '2', 5, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868083245056, 'G5114', 1, '02', 'A', '2', 6, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868095827968, 'G5114', 1, '02', 'B', '2', 7, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868116799488, 'G5114', 1, '02', 'C', '2', 8, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868137771008, 'G5114', 1, '02', 'D', '2', 9, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868158742528, 'G5114', 1, '02', 'F', '2', 10, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868179714048, 'G5114', 1, '03', 'A', '2', 11, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868196491264, 'G5114', 1, '03', 'B', '2', 12, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868213268480, 'G5114', 1, '03', 'C', '2', 13, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868234240000, 'G5114', 1, '03', 'D', '2', 14, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868255211520, 'G5114', 1, '03', 'F', '2', 15, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868267794432, 'G5114', 2, '01', 'A', '1', 1, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868292960256, 'G5114', 2, '01', 'C', '1', 2, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868309737472, 'G5114', 2, '01', 'D', '1', 3, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868322320384, 'G5114', 2, '01', 'F', '1', 4, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868343291904, 'G5114', 2, '02', 'A', '1', 5, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868360069120, 'G5114', 2, '02', 'C', '1', 6, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868381040640, 'G5114', 2, '02', 'D', '1', 7, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868397817856, 'G5114', 2, '02', 'F', '1', 8, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868418789376, 'G5114', 2, '03', 'A', '1', 9, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868439760896, 'G5114', 2, '03', 'C', '1', 10, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868452343808, 'G5114', 2, '03', 'D', '1', 11, '2025-04-13 09:36:00.952', null);
INSERT INTO business.train_seat (id, train_code, carriage_index, `row`, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1911231868469121024, 'G5114', 2, '03', 'F', '1', 12, '2025-04-13 09:36:00.952', null);
