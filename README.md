# Blog Search 프로젝트

검색 오픈 API를 활용해서 통합 인터페이스로 검색과 데일리 인기 검색어 API를 제공합니다.

## Quick Start

- Executable Jar 링크 : 
https://github.com/lucas-factory/blog-search/blob/main/build/blog-search.jar
- Java 17 환경에서 실행
- ```java -jar blog-search.jar```

##  API Reference

Postman 호출 샘플 문서 : https://documenter.getpostman.com/view/2322914/2s93RL2bxY

### 1. 블로그 검색

#### 기본 정보

```shell
GET /bss/v1/search/web
```
다음 검색 서비스에서 질의어로 웹 문서를 검색합니다. 다음 검색 서비스 장애 발생 시, 네이버 검색 서비스가 연동됩니다.

#### Request

##### Parameter
| Name  | Type   | Description                                                 |
|-------|--------|-------------------------------------------------------------|
| query | String | 검색을 원하는 질의어. 최대 128자 제한                                     |
| sort  | String | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값: accuracy |
| page | Integer | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1                               |
| size | Integer | 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10                        |

#### Response

##### meta
| Name           | Type    | Description                  |
|----------------|---------|------------------------------|
| total_count    | Integer | 검색된 문서 수                     |
| pageable_count | Integer | total_count 중 노출 가능 문서 수     |
| is_end         | Boolean | 현재 페이지가 마지막 페이지인지 여부         |
| publisher      | String  | 검색 결과를 제공한 서비스 (kakao/naver) |

##### documents
| Name     | Type     | Description                                  |
|----------|----------|----------------------------------------------|
| title    | String   | 문서 제목                                        |
| contents | String   | 문서 본문 중 일부                                   |
| url      | String   | 문서 URL                                       |
| datetime | Datetime | 문서 글 작성시간 ([YYYY]-[MM]-[DD]T[hh]:[mm]:[ss]+[tz]) |

### 2. 블로그 검색 (검색 엔진 지정)

#### 기본 정보

```shell
GET /bss/v1/search/web/{publisher}
```
Path Parameter로 검색 엔진을 설정하여 웹 문서를 검색합니다.

#### Request
##### Path Parameter
| Name      | Type   | Description                  |
|-----------|--------|------------------------------|
| publisher | String | 검색하고자 하는 엔진 설정 (kakao/naver) |

##### Parameter
| Name  | Type   | Description                                                 |
|-------|--------|-------------------------------------------------------------|
| query | String | 검색을 원하는 질의어. 최대 128자 제한                                     |
| sort  | String | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값: accuracy |
| page | Integer | 결과 페이지 번호, 1~50 사이의 값, 기본 값 1                               |
| size | Integer | 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10                        |

#### Response

##### meta
| Name           | Type    | Description                  |
|----------------|---------|------------------------------|
| total_count    | Integer | 검색된 문서 수                     |
| pageable_count | Integer | total_count 중 노출 가능 문서 수     |
| is_end         | Boolean | 현재 페이지가 마지막 페이지인지 여부         |
| publisher      | String  | 검색 결과를 제공한 서비스 (kakao/naver) |

##### documents
| Name     | Type     | Description                                  |
|----------|----------|----------------------------------------------|
| title    | String   | 문서 제목                                        |
| contents | String   | 문서 본문 중 일부                                   |
| url      | String   | 문서 URL                                       |
| datetime | Datetime | 문서 글 작성시간 ([YYYY]-[MM]-[DD]T[hh]:[mm]:[ss]+[tz]) |

### 3. 데일리 인기 검색어 조회

#### 기본 정보

```shell
GET /bss/v1/search/keywords/top10/daily
```

24시간 하루치의 인기 검색어를 조회합니다. 최대 10개.

#### Response

##### meta
| Name        | Type     | Description  |
|-------------|----------|--------------|
| total_count | Integer  | 인기 검색어 결과 개수 |
| begin       | DateTime | 기준 시작 날짜     |
| end         | DateTime | 기준 종료 날짜     |

##### documents
| Name    | Type    | Description |
|---------|---------|-------------|
| keyword | String  | 검색어         |
| count   | Integer | 검색 횟수       |
