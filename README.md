Pink Tube
===
<img src="https://github.com/y-media/pinktube/assets/139103652/452744fe-4a23-4e96-b269-08f22e3961e9" width="300" height="300">

- 팀명 :  **2(E)는없조**
- 팀 소개 : **전원 I로 이루어진 조..**
- 팀 노션 : **https://www.notion.so/2-E-6717f5205b2c452fae07f13b9b7a2d1a**

| 이름   | 역할 | MBTI        | BLOG                                               | GitHub                                                   | 
| ------ | ---- | ----------  | -------------------------------------------------- | -------------------------------------------------------- |
| 박용석 | 팀장 | ISFJ         | https://velog.io/@ys4897                           |      https://github.com/yspark2                          |
| 권민석 | 팀원 | ISFP         | https://coding-martinkwon.tistory.com/             |  https://github.com/MartinKwon94                         |
| 박준수 | 팀원 | INFP         | https://velog.io/@subak                            |    https://github.com/subak96                            |
| 신승철 | 팀원 | ISFP         | https://velog.io/@tlstmdcjfekt                     |  https://github.com/developShin                          |
| 김민종 | 팀원 | ISTJ         | https://aaapple.tistory.com/                       |     https://github.com/Kim-Min-Jong                      |

### 와이어프레임
![image](https://github.com/y-media/pinktube/assets/139103652/15b7e06a-e7cc-4903-a50e-e2fae395f1c0)
![image](https://github.com/y-media/pinktube/assets/139103652/606aee4c-95bb-4a7e-a5a1-89ff8ce551f5)

### 기본 화면 구성
- 기본 화면 구성
    TabLayout + ViewPager2를 조합해서 사용
### 1. Home Page
![image](https://github.com/y-media/pinktube/assets/139103652/41e919c7-2d90-4870-b58a-28b4d01ae571)
- [x]  사용자에게 YouTube의 현재 인기 및 새로운 콘텐츠를 중점적으로 보여주는 핵심 화면
- [x]  아래 목록을 스크롤이 가능한 RecyclerView 형태로 나열하여 출력
- [x]  **Most Popular Videos 목록** 보여주고 수평으로 스크롤이 되도록 구현
- [x]  **Category Videos 목록** 보여주고 수평으로 스크롤이 되도록 구현
- [x]  **Category Channels 목록** 보여주고 수평으로 스크롤이 되도록 구현

### 2. Search Page
![image](https://github.com/y-media/pinktube/assets/139103652/25022286-2032-4d10-bdee-03d7d9fac536)
- [x]  사용자가 원하는 비디오를 쉽게 검색하고 결과를 빠르게 확인할 수 있는 기능을 제공
- [x]  상단에는 검색을 위한 **`Search EditText`**를 배치하고, 그 아래에 검색 결과를 출력할 **`RecyclerView`**를 배치
- [x]  격자 구조의 형태로 결과를 배치
- [x]  각 아이템에는 영상 정보(제목, 영상 길이, 조회 수 등)를 함께 보여주어야 함


### 3. Detail Page
![image](https://github.com/y-media/pinktube/assets/139103652/4b62045c-f5cf-4314-b027-79fff0ae5b44)
- [x]  각 비디오 아이템 선택 시 Detail로 이동하여 선택된 비디오의 상세 정보를 제공
- [x]  **"좋아요" 버튼 추가**
- [x]  **My Video 저장**
- [x]  Detail page 시작과 종료시 특별한 Effect (화면 전환 애니메이션)
- [x]  댓글 버튼 누를 시 영상의 댓글을 불러와 보여주기 (새로운 뷰 만들기)
- [x]  homefragment에서 불러온 영상 재생하기
- [x]  링크 공유하기 기능

### 4. My Page
![image](https://github.com/y-media/pinktube/assets/139103652/aafb6c54-310b-4fdb-8437-7a0120d59155)
- [x]  사용자의 개인 정보 및 사용자가 ‘좋아요’를 누른 비디오 목록을 보여주는 기능 제공
- [x]  사용자의 프로필 사진, 이름 등의 개인 정보를 상단에 표시
- [x]  ‘좋아요’를 누른 비디오 목록은 **`RecyclerView`**를 사용해 아래쪽에 목록 형태로 출력
- [x]  "좋아요" 버튼을 통해 추가된 비디오는 내부 (예: Room or SharedPreference)에  저장되어야 하며, **`MyVideoFragment`**에서는 이 정보를 가져와서 표시

### 5. Shorts Page
![image](https://github.com/y-media/pinktube/assets/139103652/da21c06b-64b7-4ccd-8d56-113860bbee8f)
- [x]  동영상의 길이 60초 이내인 비디오 목록을 보여주는 기능 제공
- [x]  쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능 제공(무한 스크롤)
- [x]  `좋아요` 버튼 클릭 시 `My Video` 에 저장
- [x]  댓글 버튼 누를 시 영상의 댓글을 불러와 보여주기 (새로운 뷰 만들기)
- [x]  공유 버튼 누를 시 , 영상 제목과 링크를 공유










