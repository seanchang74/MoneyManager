# MoneyManager
Android application enables users to record their income and expense every day

### Download APK 
Please Click [Here](https://drive.google.com/file/d/1KKSfDscfoB6HN96ZA2PqexzbckM13ePV/view?usp=share_link)

### Technique
- Using Widget
    - Intent 
    - TabLayout, ViewHolder
    - CardView + RecyclerView
    - Fragment, FragmentAdapter
    - FloatingActionButton
    - Piechart display statistic data
- Implement MVC structure
    - Model: manage record data through **Expense** class
    - View: using Fragment to adjust the hierarcy in main page
    - Controller: store some listener and function

### Features
- [x] Filter records by datetimepicker in main page
- [x] Divide expense and income data by colors(blue is expense, red is income)
- [x] Display your expense status through piechart 
- [x] Support CRUD function in my application

### Screenshot

| **Init Main Page** | **Init Stat Page** | **Income Page** | **Expense Page** |
|:---------:|:--------:|:--------:|:--------:|
|![](https://i.imgur.com/Zw4mNRx.jpg)| ![](https://i.imgur.com/5A40puF.jpg) | ![](https://i.imgur.com/akX2Y4N.jpg)| ![](https://i.imgur.com/0MY9gtY.jpg)|
| **Main Page** | **Stat Page** | **Update Data** | **Delete Data** |
| ![](https://i.imgur.com/VmDBlB5.jpg) | ![](https://i.imgur.com/cLLS1lc.jpg)| ![](https://i.imgur.com/iON8xs4.jpg) | ![](https://i.imgur.com/ANUmQ6G.jpg)|

### System Architecture 
![](https://i.imgur.com/Dwg18KQ.png)
* Expense -> The class for records
* MyDBHelper -> Store the database CRUD function
* ExpenseFragment -> The Fragment of records
* FragmentAdapter -> Combine ViewHolder with TabLayout
* PersonalFragment -> Developer Info 
* StatisticFragment -> Display piechart 
* Edit_New_Cost -> Edit page for new records
* MainActivity -> Main Framework of this application 
* New_Cost -> create a new record
---
![](https://i.imgur.com/y79YPhu.png)
- anim -> FAB animation file
- drawable -> icon file
- layout -> layout file
- mipmap -> customize icon image 
- values -> parameter values

###### tags: `GitHub Readme`
