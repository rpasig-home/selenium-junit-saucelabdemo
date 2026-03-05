# Runs full Selenium + Allure stack
test:
	# Clean previous containers and volumes
	docker compose down -v

	# Start infrastructure (selenium, report)
	docker compose up -d selenium report

	# Run tests and remove container after
	docker compose build --no-cache tests
	docker compose run --rm tests