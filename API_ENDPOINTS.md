# API Endpoints Documentation

이 문서는 각 컨트롤러별로 노출되는 API 엔드포인트와 관련 정보를 정리한 것입니다.

---

## AuthController (`/api/auth`)
- **POST** `/api/auth/login`
    - 로그인 API
    - **요청**: `LoginRequestDto`
    - **응답**: `LoginResponseDto` (accessToken, refreshToken 포함)
- **POST** `/api/auth/refresh-token`
    - 액세스 토큰 갱신 API
    - **요청**: `RefreshTokenRequest`
    - **응답**: `LoginResponseDto`
- **POST** `/api/auth/logout`
    - 로그아웃 API
    - **헤더**: `USER_NAME`
- **POST** `/api/auth/validate`
    - 토큰 유효성 검사 및 사용자 정보 반환 API
    - **헤더**: `Authorization` (Bearer 토큰)

> **총 엔드포인트**: 4

---

## CompanyController (`/api/companies`)
- **POST** `/api/companies`
    - 회사 생성 API
    - **요청**: `CompanyCreateRequestDto`
    - **응답**: `CompanyResponseDto`
- **PUT** `/api/companies/{companyId}`
    - 회사 수정 API
    - **요청**: `CompanyUpdateRequestDto`
    - **응답**: `CompanyResponseDto`
- **GET** `/api/companies`
    - 회사 리스트 조회 (페이지네이션 지원)
- **GET** `/api/companies/{companyId}`
    - 회사 단건 조회 API
- **DELETE** `/api/companies/{companyId}`
    - 회사 삭제 API
- **GET** `/api/companies/search`
    - 회사 검색 API
    - **쿼리 파라미터**: `name`, `hubId`, `type`, `page`, `size`, `sortDirection`, `sortBy`
- **GET** `/api/companies/info/id/{companyId}`
    - 회사 상세 정보 조회 API (특정 포맷)

> **총 엔드포인트**: 7

---

## DeliveryController (`/api/deliveries`)
- **POST** `/api/deliveries`
    - 배송 생성 API
    - **요청**: `DeliveryCreateRequestDto`
    - **응답**: `DeliveryResponseDto`
- **GET** `/api/deliveries/{deliveryId}`
    - 배송 단건 조회 API
- **GET** `/api/deliveries`
    - 배송 리스트 조회 (페이지네이션 지원)
- **PUT** `/api/deliveries/{deliveryId}`
    - 배송 수정 API
    - **요청**: `DeliveryUpdateRequestDto`
    - **응답**: `DeliveryResponseDto`
- **DELETE** `/api/deliveries/{deliveryId}`
    - 배송 삭제 API
- **GET** `/api/deliveries/search`
    - 배송 검색 API
    - **요청 DTO**: `DeliverySearchRequestDto`

> **총 엔드포인트**: 6

---

## DeliveryRouteController (`/api/deliveries`)
- **POST** `/api/deliveries/route/depart`
    - 배송 경로 출발 정보 등록 API
    - **요청**: `DeliveryRouteDepartRequestDto`
- **POST** `/api/deliveries/route/arrive`
    - 배송 경로 도착 정보 등록 API
    - **요청**: `DeliveryRouteArriveRequestDto`
- **GET** `/api/deliveries/{deliveryId}/route`
    - 특정 배송의 경로 리스트 조회 API
- **GET** `/api/deliveries/{orderId}/order/route`
    - 주문 ID에 해당하는 배송 경로 리스트 조회 API

> **총 엔드포인트**: 4

---

## DeliveryManagerController (`/api/managers`)
- **POST** `/api/managers`
    - 배송 담당자 생성 API
    - **요청**: `CreateDeliveryManagerRequestDto`
    - **응답**: `DeliveryManagerResponse`
- **GET** `/api/managers/{userId}`
    - 배송 담당자 단건 조회 API
- **GET** `/api/managers`
    - 모든 배송 담당자 리스트 조회 API (페이지네이션 지원)
- **GET** `/api/managers/hub`
    - 허브 배송 담당자 리스트 조회 API
- **GET** `/api/managers/company/{hubId}`
    - 업체 배송 담당자 리스트 조회 API (허브 ID 기준)
- **DELETE** `/api/managers/userId`
    - 배송 담당자 삭제 API (요청 파라미터로 `userId` 전달)
- **PUT** `/api/managers/userId`
    - 배송 담당자 수정 API (요청 파라미터로 `userId` 전달)
- **GET** `/api/managers/search`
    - 배송 담당자 검색 API (페이지네이션, 정렬, `keyword`)

> **총 엔드포인트**: 8

---

## HubController (`/api/hubs`)
- **POST** `/api/hubs`
    - 허브 생성 API
    - **요청**: `CreateHubRequestDto`
    - **응답**: `HubResponseDto`
- **GET** `/api/hubs/{hubId}`
    - 허브 단건 조회 API
- **GET** `/api/hubs`
    - 허브 리스트 조회 API (페이지네이션 지원)
- **PUT** `/api/hubs/{hubId}`
    - 허브 수정 API
    - **요청**: `UpdateHubRequestDto`
    - **응답**: `HubResponseDto`
- **DELETE** `/api/hubs/{hubId}`
    - 허브 삭제 API
- **GET** `/api/hubs/search`
    - 허브 검색 API (페이지네이션, 정렬, `keyword`)
- **GET** `/api/hubs/info/id/{hubId}`
    - 허브 정보 조회 API (허브 ID 기준)
- **GET** `/api/hubs/info/managerId/{managerId}`
    - 허브 정보 조회 API (관리자 ID 기준)

> **총 엔드포인트**: 8

---

## RouteController (`/api/routes`)
- **POST** `/api/routes/init`
    - 허브 간 이동정보 초기화 API
- **GET** `/api/routes/shortest-path`
    - 최단 경로 조회 API
    - **쿼리 파라미터**: `departureHubId`, `arrivalHubId`
- **POST** `/api/routes`
    - 허브 간 이동정보 생성 API
    - **요청**: `CreateRouteRequestDto`
    - **응답**: `RouteResponse`
- **GET** `/api/routes`
    - 허브 간 이동정보 단건 조회 API
    - **쿼리 파라미터**: `departureHubId`, `arrivalHubId`
- **GET** `/api/routes/list`
    - 허브 간 이동정보 리스트 조회 API (페이지네이션 지원)
- **PUT** `/api/routes`
    - 허브 간 이동정보 수정 API
    - **요청**: `UpdateRouteRequestDto`
    - **응답**: `RouteResponse`
- **DELETE** `/api/routes`
    - 허브 간 이동정보 삭제 API
    - **쿼리 파라미터**: `departureHubId`, `arrivalHubId`
- **GET** `/api/routes/search`
    - 허브 간 이동정보 검색 API (페이지네이션, 정렬, `keyword`)

> **총 엔드포인트**: 8

---

## MessageController (`/api/messages`)
- **POST** `/api/messages`
    - 메세지 전송 API
    - **요청**: `PostMessageDto`
- **PATCH** `/api/messages/{messageId}`
    - 메세지 수정 API
    - **요청**: 메시지 내용 (String)
- **DELETE** `/api/messages/{messageId}`
    - 메세지 삭제 API
- **GET** `/api/messages/{messageId}`
    - 메세지 단건 조회 API
- **GET** `/api/messages/list`
    - 메세지 리스트 조회 API (페이지네이션 지원)
- **GET** `/api/messages/search`
    - 메세지 검색 API (페이지네이션, 정렬, `keyword`)

> **총 엔드포인트**: 6

---

## OrderController (`/api/orders`)
- **POST** `/api/orders`
    - 주문 생성 API
    - **요청**: `OrderCreateRequestDto`
    - **응답**: `ApiResponse<OrderResponseDto>`
- **PATCH** `/api/orders/{orderId}`
    - 주문 수정 API
    - **요청**: `OrderUpdateRequestDto`
    - **응답**: `ApiResponse<OrderResponseDto>`
- **DELETE** `/api/orders/{orderId}`
    - 주문 삭제 API
- **GET** `/api/orders/{orderId}`
    - 주문 단건 조회 API
    - **응답**: `ApiResponse<OrderResponseDto>`
- **GET** `/api/orders`
    - 주문 리스트 조회 API (페이지네이션 지원)
    - **응답**: `ApiResponse<Page<OrderResponseDto>>`
- **GET** `/api/orders/search`
    - 주문 검색 API (페이지네이션, 정렬, `keyword`)
    - **응답**: `ApiResponse<Page<OrderResponseDto>>`
- **PATCH** `/api/orders/cancel/{orderId}`
    - 주문 취소 API

> **총 엔드포인트**: 7

---

## ProductController (`/api/products`)
- **POST** `/api/products`
    - 상품 생성 API
    - **요청**: `ProductCreateRequestDto`
    - **응답**: `ProductResponseDto`
- **PATCH** `/api/products/{productId}`
    - 상품 수정 API
    - **요청**: `ProductUpdateRequestDto`
    - **응답**: `ProductResponseDto`
- **DELETE** `/api/products/{productId}`
    - 상품 삭제 API
- **GET** `/api/products/{productId}`
    - 상품 단건 조회 API
    - **응답**: `ProductResponseDto`
- **GET** `/api/products`
    - 상품 리스트 조회 API (페이지네이션 지원)
    - **응답**: `Page<ProductResponseDto>`
- **GET** `/api/products/search`
    - 상품 검색 API (페이지네이션, 정렬, `keyword`)
    - **응답**: `Page<ProductResponseDto>`

> **총 엔드포인트**: 6

---

## UserController (`/api/users`)
- **GET** `/api/users/client/{username}`
    - 클라이언트용 유저 상세 정보 조회 API (username 기준)
    - **응답**: `UserDetailsDto`
- **GET** `/api/users/client/slackId/{slackId}`
    - 클라이언트용 슬랙 ID로 username 조회 API
    - **응답**: `String` (username)
- **POST** `/api/users/sign-up`
    - 회원가입 API
    - **요청**: `UserSignUpRequestDto`
    - **응답**: `UserResponseDto`
- **PATCH** `/api/users`
    - 유저 정보 업데이트 API (헤더: X-User-Name, 요청: `UserPatchRequestForRegisterDto`)
- **GET** `/api/users`
    - 유저 개인 상세 정보 조회 API (헤더: X-User-Name)
    - **응답**: `UserResponseDto`
- **GET** `/api/users/{userId}`
    - 관리자용 유저 단건 조회 API
    - **응답**: `UserResponseForRegisterDto`
- **GET** `/api/users/list`
    - 관리자용 유저 리스트 조회 API (페이지네이션 지원)
    - **응답**: `Page<UserDataForRegisterDto>`
- **GET** `/api/users/search`
    - 관리자용 유저 검색 API (페이지네이션, 정렬, `keyword`)
    - **응답**: `Page<UserDataForRegisterDto>`
- **PATCH** `/api/users/{userId}`
    - 관리자용 유저 정보 수정 API
    - **요청**: `UserPatchRequestForRegisterDto`
    - **응답**: `UserResponseForRegisterDto`
- **DELETE** `/api/users/{userId}`
    - 관리자용 유저 삭제 API
- **GET** `/api/users/info/{userId}`
    - 유저 정보 조회 API
    - **응답**: `UserInfoResponseDto`

> **총 엔드포인트**: 11

---

# 전체 통계

- **전체 API 엔드포인트 총합**: 75개  
  (Auth: 4, Company: 7, Delivery: 6, DeliveryRoute: 4, DeliveryManager: 8, Hub: 8, Route: 8, Message: 6, Order: 7, Product: 6, User: 11)

