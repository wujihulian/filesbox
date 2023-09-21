//dateSetting.js
//将当前日期写成常量
const date = new Date();
const year = date.getFullYear();
const month = date.getMonth() + 1;
const day = date.getDate();
const hour = date.getHours();
const minute = date.getMinutes();
//确定月份呈现
function determineMonth() {
  let monthDates = [];
  for (let i = month; i <= 12; i++) {
    // if(i<10)
    // {
    //   monthDates.push()
    // }
    monthDates.push(i+"");
  }
  for (let i = 1; i < month; i++) {
    monthDates.push(i+"")
  }
  return monthDates;
}
//根据年与月确定日的呈现
function determineDay(year, month) {
  let dayDates = [];
  let days;
  switch (parseInt(month)) {
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
      days = 31;
      break;
    case 4:
    case 6:
    case 9:
    case 11:
      days = 30;
      break;
    case 2:
      if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
        days = 28
      } else days = 29;
  }
  for (let i = day; i <= days; i++) {
    dayDates.push(i+"");
  }
  for (let i = 1; i <= day; i++) {
    dayDates.push(i+"");
  }
  return dayDates;
}
//确定小时的呈现
function determineHour() {
  let hourDates = [];
  for (let i = hour; i <= 24; i++) {
    hourDates.push(i+"");
  }
  for (let i = 1; i < hour; i++) {
    hourDates.push(i+"");
  }
  return hourDates;
}
//确定分的呈现
function determineMinute() {
  let minuteDates = [];
  let minuteNum=0;
//   if (parseInt(minute) % 10 >= 1) {
//     minuteNum = (parseInt(minute / 10) + 1) * 10;
//   } else {
//     minuteNum = parseInt(minute / 10) * 10;
//   }
  minuteNum = minute+1
  for (let i = minuteNum; i < 60; i += 1) {
    minuteDates.push(i+"")
  }
  for (let i = 0; i < minuteNum; i += 1) {
    minuteDates.push(i+"")
  }
  return minuteDates;
}
//将上述函数导出，这样引入文件后可以直接使用导出的函数
module.exports = {
  determineMonth:determineMonth,
  determineDay:determineDay,
  determineHour:determineHour,
  determineMinute:determineMinute,
}