up:
	docker-compose up -d

stop:
	docker-compose stop

init:
	docker cp migrations/1.sql local-postgres-1:/1.sql
	docker exec -i local-postgres-1 psql -U postgres -d postgres -f /1.sql