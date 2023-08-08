insert into user_tb(username, password, email)
values ('ssar', '1234', 'ssar@nate.com');
insert into user_tb(username, password, email)
values ('asdf', '1234', 'asdf@nate.com');
insert into board_tb(title, content, user_id, created_at)
values ('제목1', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목3', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목4', '내용4', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목5', '내용5', 1, now());

insert into reply_tb(comment, user_id, board_id)
values ('1번글_댓글1', '1', '1');
insert into reply_tb(comment, user_id, board_id)
values ('1번글_댓글2', '1', '1');
insert into reply_tb(comment, user_id, board_id)
values ('2번글_댓글1', '2', '2');
insert into reply_tb(comment, user_id, board_id)
values ('2번글_댓글2', '2', '2');
insert into reply_tb(comment, user_id, board_id)
values ('3번글_댓글1', '1', '3');
insert into reply_tb(comment, user_id, board_id)
values ('3번글_댓글2', '2', '3');
insert into reply_tb(comment, user_id, board_id)
values ('4번글 댓글1', '1', '4');
insert into reply_tb(comment, user_id, board_id)
values ('4번글 댓글2', '2', '4');
insert into reply_tb(comment, user_id, board_id)
values ('5번글 댓글1', '1', '5');
insert into reply_tb(comment, user_id, board_id)
values ('5번글 댓글2', '2', '5');