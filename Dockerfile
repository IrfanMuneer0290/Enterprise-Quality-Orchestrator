# PROBLEM: Automation suites often fail due to missing dependencies, wrong Java versions, or environment "drift."
# SOLUTION: A multi-stage, hardened Dockerfile that packages the entire Test Engine, Maven dependencies, and the Shard-Execution logic into a single, immutable unit.
# RESULT: A "Test-as-a-Service" container that runs identically on a MacBook Air, GitHub Actions, or Oracle Cloud.

# --- STAGE 1: Dependency Caching (Optimization) ---
# PROBLEM: Downloading Maven plugins on every run wastes 5-10 minutes.
# SOLUTION: Pre-cache dependencies in a dedicated layer.
FROM maven:3.9.6-eclipse-temurin-17-alpine AS dependency-cache

WORKDIR /app
# Copy the pom.xml specifically to leverage Docker layer caching
COPY ecommerce-demoblaze/demoblaze-tests/pom.xml .

# Only download dependencies; this layer is cached unless pom.xml changes.
RUN mvn dependency:go-offline -B

# --- STAGE 2: Test Execution Engine ---
FROM maven:3.9.6-eclipse-temurin-17-alpine

# REASON: Standard Linux containers have small shared memory (/dev/shm). 
# Browsers like Chrome need large memory to prevent "Aw Snap" crashes.
# SOLUTION: Handled via 'shm_size' in Compose/Grid, but the engine stays light.
WORKDIR /app

# Copy cached dependencies from Stage 1
COPY --from=dependency-cache /root/.m2 /root/.m2

# Copy the actual test source code and resources
COPY ecommerce-demoblaze/demoblaze-tests /app

# PROBLEM: How to tell the container which shard to run?
# SOLUTION: Use Environment Variables that the Entrypoint script reads.
ENV SHARD_ID=1
ENV ENV_TARGET=qa
ENV HEADLESS=true

# RESULT: The entrypoint handles the logic we built in the YAML.
ENTRYPOINT ["sh", "-c", "mvn test -DsuiteXmlFile=src/test/resources/testng-ui-shard-${SHARD_ID}.xml -Denv=${ENV_TARGET} -Dheadless=${HEADLESS}"]
