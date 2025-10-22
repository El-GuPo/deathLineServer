up:
	docker-compose up -d

stop:
	docker-compose stop

init:
	docker cp migrations/1.sql postgres-1:/
	docker exec -i postgres-1 psql -U postgres -d postgres -f /1.sql