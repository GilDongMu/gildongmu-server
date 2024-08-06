### Project
#### 여행 친구 구하기 플랫폼  ✈️
</br>
<img src="https://github.com/user-attachments/assets/33d6d434-6452-49b0-b6e6-5d92f5c78f7d" width="600" height="400">
</br>
</br>
혼자서 가는 여행은 심심하신가요? 비싼 비용을 분담하고 싶으신가요? </br>
혼자 여행하기 부담스러우신 분들, 이런저런 여행 친구들 만나보고 싶으신 분들 그리고 여행에 관한 정보를 공유하고 싶으신 분들을 위한 사이트. </br>
‘길동무’ 에서 여행 친구도 구하고 여러가지 정보를 공유해보아요!

#### Project Period
2024-03-06 ~ 2024-05-30

#### Team
**FE** | 박준성 | 김윤수 | 임주민 | 이준기 | 김범승 | </br>
**BE** | 송원선 | 이서연 | </br>
**DESIGN**  Design | 임아현 | </br>

### Tech Stack
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-social&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-social&logo=Gradle&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-social&logo=Databricks&logoColor=white">
<br/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-social&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-social&logo=JSON Web Tokens&logoColor=white">
<br/>
<img src="https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-social&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-social&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/-MongoDB-black?style=for-the-social&logo=mongodb">
<br/>
<img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-social&logo=apache-kafka&logoColor=white">
<br />
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-social&logo=junit5&logoColor=white"> <img src="https://img.shields.io/badge/ Swagger-6DB33F?style=for-the-social&logo=swagger&logoColor=white">
<br />

- java 17
- Gradle 8.5
- Spring Boot 3.1.8

### DataBase Schema

<a href='https://www.erdcloud.com/d/BT3zpE3ZrBYM2j74D'><img width="700" height="400" src='https://github.com/user-attachments/assets/bfe1efcf-bb55-4b4f-b8ce-41d7bd4890bf' border='0'></a>


### Project Structure
<img width="600" height="300" src='https://github.com/user-attachments/assets/9eafb8b6-6c56-4515-8393-12d6a2041686' border='0'>

#### CI/CD
- develop 브랜치의 코드가 개발 서버에 지속적 통합됩니다.
- git action 에 의해 빌드된 도커 이미지는 도커 허브에 저장되며, 개발 서버에 반영됩니다.


### Directory Structure
```
 📂 gildongmu-server
 ┣ 📂 gildongmu-api
 ┃ ┣ 📂src
 ┃ ┃  ┗ 📂main
 ┃ ┃    ┗ 📂resources
 ┃ ┃       ┣ 📜application-dev.yml
 ┃ ┃       ┗ 📜application-local.yml
 ┃ ┣ 📜Dockerfile
 ┃ ┗ 📜build.gradle.kts
 ┃ ┗ 📂test
 ┣ 📂 gildongmu-chat
 ┃ ┣ 📂src
 ┃ ┃  ┗ 📂main
 ┃ ┃    ┗ 📂resources
 ┃ ┃       ┣ 📜application-dev.yml
 ┃ ┃       ┗ 📜application-local.yml
 ┃ ┣ 📜Dockerfile
 ┃ ┗ 📜build.gradle.kts
 ┃ ┗ 📂test
 ┣ 📂 gildongmu-common
 ┃  ┗ 📂src
 ┃  ┗ 📜build.gradle.kts
 ┣ 📂 gildongmu-domain
 ┃  ┗ 📂src
 ┃  ┗ 📜build.gradle.kts
 ┣ 📜settings.gradle.kts
 ┣ 📂 .github
 ┃  ┗ 📂workflows
 ┃      ┗ 📜deploy-dev-api.yml
 ┃      ┗ 📜deploy-dev-chat.yml
 ┗ 📜.gitignore

```
</br>
길동무 프로젝트는 두 개의 서버 모듈과 두 개의 라이브러리 모듈로 구성됩니다.
</br> </br>

- `📂gildongmu-api` : 서버 모듈로, 서비스의 모든 RESTFul 관련 엔드포인트 및 로직을 포함합니다.
- `📂gildongmu-chat` : 서버 모듈로, 실시간 통신에 필요한 웹소켓 관련 엔드포인트 및 로직을 포함합니다.
- `📂gildongmu-domain` : 라이브러리 모듈로, 프로젝트 전반에 사용되는 Entity 와 Repository 를 포함합니다.
- `📂gildongmu-common` : 라이브러리 모듈로, 여러 모듈에서 공통으로 활용하는 기능에 대한 로직을 포함합니다.
- `📂**/📜application-dev.yml` : 개발 환경에 반영될 환경변수를 세팅합니다.
- `📂**/📜application-local.yml` : 로컬 환경에 반영될 환경변수를 세팅합니다.
- `📜settings.gradle` : 하위 모듈을 선언합니다.
- `📜.gitignore` : git 에 올라가지 않아야 할 파일을 정의합니다.
- `📂**/📜Dockerfile` : 서버 모듈을 도커 이미지로 빌드하기 위한 파일입니다.
- `📂**/📜deploy-dev-api.yml` : api 모듈을 지속적으로 도커 이미지로 빌드/푸시/배포합니다.
- `📂**/📜deploy-dev-chat.yml` : chat 모듈을 지속적으로 도커 이미지로 빌드/푸시/배포합니다.

### Convention
#### Branch Strategy
```
main
├─hotfix
└─ develop (default)
    └─ DOMAIN/이슈번호
```

#### Commit Message
```javascript
<type>: <description>

[optional body]
```

#### Commit Type
| type      | 설명                                               |
|-----------|--------------------------------------------------|
| `feat`    | A new feature                                    |
| `test`    | Adding new test or making changes to existing test |
| `fix`     | A bug fix                                        |
| `perf`    | A code that improves performance                 |
| `docs`    | Documentation a related changes                  |
| `refactor` | Changes for refactoring                      |
| `build`   | Changes related to building the code             |
| `chore`   | Changes that do not affect the external user     |
