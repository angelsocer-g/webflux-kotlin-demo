정해진 조건의 문서를 ES에서 주기적으로 조회하고, 필요한 정보를 가공한 뒤 PostgreSQL DB에 저장한다,

flow
[Scheduler]
↓
[Service Layer - Coroutine Flow 시작]
↓
[ES Client - Query with Coroutine]
↓
[Document Parsing & Mapping (DTO -> Entity)]
↓
[R2DBC Repository - Coroutine 기반 DB 저장]
↓
[Result Logging / Error Handling]