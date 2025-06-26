# 프로젝트 개요

## 기술 스택

### 주요 플랫폼 및 언어
- Java 21
- Kotlin 1.9.25
- Spring Boot 3.5.3

### 의존성 목록

#### 핵심 의존성
- **Spring WebFlux**: 리액티브 웹 애플리케이션 개발
- **Spring Data R2DBC**: 리액티브 데이터베이스 액세스
- **Spring Boot Validation**: 데이터 유효성 검증
- **PostgreSQL**: 데이터베이스 (R2DBC 드라이버 포함)

#### Kotlin 관련 의존성
- **kotlin-reflect**: Kotlin 리플렉션 라이브러리
- **kotlinx-coroutines-reactor**: 코루틴과 Project Reactor 통합
- **jackson-module-kotlin**: Jackson의 Kotlin 지원
- **reactor-kotlin-extensions**: Project Reactor의 Kotlin 확장

#### 개발 도구
- **spring-boot-devtools**: 개발 생산성 향상 도구

#### 테스트 의존성
- **spring-boot-starter-test**: 스프링 부트 테스트 프레임워크
- **reactor-test**: 리액티브 스트림 테스트 도구
- **kotlin-test-junit5**: Kotlin JUnit 5 테스트 지원
- **kotlinx-coroutines-test**: 코루틴 테스트 지원
- **junit-platform-launcher**: JUnit 테스트 실행 도구

## 빌드 정보
- 빌드 도구: Gradle (Kotlin DSL)
- 의존성 관리: Spring Dependency Management Plugin 1.1.7
