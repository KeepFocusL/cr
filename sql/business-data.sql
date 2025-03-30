-- 车站表
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906233878281064448, '福州', 'fuzhou', 'fz', '2025-03-30 14:35:47.352', '2025-03-30 14:35:47.352');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906233939383685120, '福州南', 'fuzhounan', 'fzn', '2025-03-30 14:36:01.920', '2025-03-30 14:36:01.920');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906234218124546048, '泉州南', 'quanzhounan', 'qzn', '2025-03-30 14:37:08.377', '2025-03-30 14:37:08.377');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906234279713705984, '厦门北', 'xiamenbei', 'xmb', '2025-03-30 14:37:23.061', '2025-03-30 14:37:23.061');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906234323292524544, '厦门', 'xiamen', 'xm', '2025-03-30 14:37:33.451', '2025-03-30 14:37:33.451');
INSERT INTO business.station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1906234394851545088, '北京', 'beijing', 'bj', '2025-03-30 14:37:50.512', '2025-03-30 14:37:50.512');


-- 车次
INSERT INTO business.train (id, code, type, start, start_pinyin, start_time, end, end_pinyin, end_time, created_at, updated_at) VALUES (1906235345352134656, 'G5115', 'G', '福州', 'fuzhou', '11:42:00', '厦门', 'xiamen', '13:25:00', '2025-03-30 14:41:37.128', '2025-03-30 14:41:37.128');


-- 火车车站
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1906235697682059264, 'G5115', 1, '福州', 'fuzhou', '11:42:00', '11:42:00', '00:00:00', 0.00, '2025-03-30 14:43:01.000', '2025-03-30 14:43:08.662');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1906236277292929024, 'G5115', 2, '福州南', 'fuzhounan', '11:57:00', '12:00:00', '00:03:00', 18.00, '2025-03-30 14:45:19.000', '2025-03-30 14:45:43.970');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1906236581245751296, 'G5115', 3, '泉州南', 'quanzhounan', '12:41:00', '12:43:00', '00:02:00', 171.00, '2025-03-30 14:46:31.000', '2025-03-30 14:49:29.748');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1906236779258843136, 'G5115', 4, '厦门北', 'xiamenbei', '13:02:00', '13:04:00', '00:02:00', 59.00, '2025-03-30 14:47:18.000', '2025-03-30 14:49:03.758');
INSERT INTO business.train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1906237077394165760, 'G5115', 5, '厦门', 'xiamen', '13:25:00', '13:25:00', '00:00:00', 31.00, '2025-03-30 14:48:30.080', '2025-03-30 14:48:30.080');


-- 火车车厢（不完整）
INSERT INTO business.train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1906238540719722496, 'G5115', 1, '2', 10, 2, 5, '2025-03-30 14:54:18.964', '2025-03-30 14:54:18.964');
INSERT INTO business.train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1906238613486702592, 'G5115', 2, '1', 8, 2, 4, '2025-03-30 14:54:36.313', '2025-03-30 14:54:36.313');


-- 火车座位（不完整）
INSERT INTO business.train_seat (id, train_code, carriage_index, row, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1906239430864277504, 'G5115', 1, '1', 'A', '2', 1, '2025-03-30 14:57:51.191', '2025-03-30 14:57:51.191');
INSERT INTO business.train_seat (id, train_code, carriage_index, row, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1906239574456274944, 'G5115', 1, '1', 'B', '2', 2, '2025-03-30 14:58:25.426', '2025-03-30 14:58:25.426');
INSERT INTO business.train_seat (id, train_code, carriage_index, row, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1906239654580064256, 'G5115', 1, '1', 'C', '2', 3, '2025-03-30 14:58:44.529', '2025-03-30 14:58:44.529');
INSERT INTO business.train_seat (id, train_code, carriage_index, row, col, seat_type, carriage_seat_index, created_at, updated_at) VALUES (1906239741251162112, 'G5115', 1, '2', 'A', '2', 6, '2025-03-30 14:59:05.193', '2025-03-30 14:59:05.193');
