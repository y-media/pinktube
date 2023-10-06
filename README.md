<img src="https://github.com/y-media/pinktube/assets/139103652/452744fe-4a23-4e96-b269-08f22e3961e9" width="300" height="300">
 
# 📺 PINKTUBE 🎞

## 🎈 Team Introduce 🎈
- ### [Team Notion]<https://www.notion.so/2-E-6717f5205b2c452fae07f13b9b7a2d1a>

- ### [Team GitHub]<https://github.com/y-media/pinktube>

- ### [Canva]<https://www.canva.com/design/DAFwTIBILho/2GmYVZDvnnnt3G2f4-i6MA/view?utm_content=DAFwTIBILho&utm_campaign=designshare&utm_medium=link&utm_source=publishsharelink>

| 이름   | 역할 | MBTI        | BLOG                                               | GitHub                                                   | 
| ------ | ---- | ----------  | -------------------------------------------------- | -------------------------------------------------------- |
| 박용석 | 팀장 | ISFJ         | https://velog.io/@ys4897                           |      https://github.com/yspark2                          |
| 권민석 | 팀원 | ISFP         | https://coding-martinkwon.tistory.com/             |  https://github.com/MartinKwon94                         |
| 박준수 | 팀원 | INFP         | https://velog.io/@subak                            |    https://github.com/subak96                            |
| 신승철 | 팀원 | ISFP         | https://velog.io/@tlstmdcjfekt                     |  https://github.com/developShin                          |
| 김민종 | 팀원 | ISTJ         | https://aaapple.tistory.com/                       |     https://github.com/Kim-Min-Jong                      |


<details>
<summary>역할 분담</summary>
  
![image](https://github.com/y-media/pinktube/assets/139086025/8efd8509-984a-4963-a388-3752110b4166)


</details>

## 🎩 Project Introduce 🎩
### 기본 화면 구성

<details>
<summary>와이어 프레임</summary>
  
![image](https://github.com/y-media/pinktube/assets/139103652/15b7e06a-e7cc-4903-a50e-e2fae395f1c0)
![image](https://github.com/y-media/pinktube/assets/139103652/606aee4c-95bb-4a7e-a5a1-89ff8ce551f5)

회의를 통하여 구체적인 설계에 들어가기 전에 `대략적인 틀`을 구성하였습니다.
</details>

<details>
<summary>Splash Page</summary>

<img src ="https://github.com/cording10jianzo/B.F-Baby_Friend-/assets/88123219/0957072f-c151-44af-8944-5409d3782a6a" width="200" height="350"/>

앱이 실행될 때 시작화면으로 `lottie animation`을 추가했습니다.

</details>
    
<details>
<summary>Home Page</summary>

<img src ="https://github.com/y-media/pinktube/assets/88123219/72023010-f5d9-4d29-b3ce-ab5e1c9399ed" width="200" height="350"/>

사용자에게 YouTube의 현재 인기 및 새로운 콘텐츠를 중점적으로 보여주는 핵심 화면입니다.

TabLayout + ViePager2 사용하여 구현하였습니다.

아래 목록을 스크롤이 가능한 RecyclerView 형태로 나열하여 출력합니다.

`Most Popular Videos 목록` 보여주고 수평으로 스크롤이 되도록 구현했습니다.

`Category Videos 목록` 보여주고 수평으로 스크롤이 되도록 구현했습니다.

`Category Channels 목록` 보여주고 수평으로 스크롤이 되도록 구현했습니다.

</details>

<details>
<summary>Detail Page</summary>

<img src ="https://github.com/y-media/pinktube/assets/88123219/dbfba8e5-6d46-44e6-b40f-9e95ee2f57d8" width="200" height="350"/>

Home Page에서 각 아이템 선택시 선택된 비디오의 `상세 정보 제공`합니다.

`좋아요` 버튼 클릭 시 My Page에 비디오 정보를 `저장`합니다. 

Detail page 시작과 종료시 특별한 Effect로 `화면 전환 애니메이션`을 적용했습니다.

댓글 버튼 누를 시 `영상의 댓글`을 불러와 보여주기를 구현했습니다.

homefragment에서 불러온 영상 `재생`하기가 가능합니다.

링크 `공유하기` 기능을 추가했습니다.

</details>

<details>
<summary>Search Page</summary>

<img src ="https://github.com/y-media/pinktube/assets/88123219/dcf5ce0b-169c-4584-a795-35ed76328eec" width="180" height="350"/>

사용자가 원하는 비디오를 쉽게 `검색`하고 `결과`를 빠르게 확인할 수 있는 기능을 제공합니다.

상단에는 검색을 위한 `Search EditText`를 배치하고, 그 아래에 검색 결과를 출력할 `RecyclerView`를 배치했습니다.

격자 구조의 형태로 결과를 배치했습니다.

각 아이템에는 영상 정보(`제목, 영상 길이, 조회 수 등`)를 함께 보여줍니다.

</details>

<details>
    
<summary>My Page</summary>

<img src ="https://github.com/y-media/pinktube/assets/88123219/8d2af801-46d2-4978-ad4f-cf80757c4b0a" width="200" height="350"/>

사용자의 개인 정보 및 사용자가 `좋아요`를 누른 비디오 목록을 보여주는 기능을 제공합니다.

사용자의 프로필 사진, 이름 등의 `개인 정보를 상단에 표시`합니다.

`좋아요`를 누른 비디오 목록은 `RecyclerView`를 사용해 아래쪽에 목록 형태로 출력합니다.

저장과 삭제에는 `Room database`를 적용하였습니다.

`롱 클릭시 삭제 기능`을 추가했습니다.

</details>
<details>
<summary>Shorts Page</summary>

<img src ="https://github.com/y-media/pinktube/assets/88123219/9d4d4f87-fac5-4b84-a37e-73fe0b6ded35" width="200" height="350"/>

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능을 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>


## 📗 Platforms & Languages 📒
<img src="https://img.shields.io/badge/android-3DDC84?style=flat-square&logo=android&logoColor=white"/>  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>

## 📕 Tools 📘
<img src="https://img.shields.io/badge/figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/>  <img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white"/>  <img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white"/>  <img src="https://img.shields.io/badge/notion-000000?style=flat-square&logo=notion&logoColor=white"/> 

