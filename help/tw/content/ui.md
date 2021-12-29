### 界面說明

整個界分為幾大塊，最上面的是操作區，即菜單和工具欄，中間則是數據區，用來展示指定站點的視頻信息，要強調的是，它可以在瀏覽器和表格兩種方式中隨意切換，兩者的查詢條件也各有不同，具體操作後面會詳細介紹。最下面的是下載，錯誤，任務等視圖，方便用戶管理。

當第一次打開某個站點的時候，一般會提示該站點內的視頻列表信息為空，會提示用戶是否現在就從網站導入。有時候導入會非常慢，可能因為網站有大量視頻，導入自然慢；以及很多網站為了攔截爬蟲，會規定在多長時間內訪問超出指定次數，就會拒絕訪問，所以默認訪問網頁的頻度加了限制。但不用擔心，導入是後台默默進行的工作，而且每導入若干條視頻信息，都會自動刷新界面並顯示出來，並不會影響操作。雖然程序盡量使用了後台線程來加載網頁，但由於使用了瀏覽器加以訪問，還是對整個程序的界面操作速度有一定的影響，所以如果可能的話，考慮在空閒時間來導入視頻。

**使用瀏覽器查看視頻信息：**

1.  瀏覽器界面裡可以隨時通過右側的界面來查找自己喜歡的視頻，包括關鍵字/演員/分類等信息。
2.  瀏覽器界面裡最上面是分頁功能，還可以通過左右鍵盤快速跳轉上一頁或者下一頁。
3.  瀏覽器界面裡可以單擊鼠標選中或者撤消選中指定的視頻。對於選中的視頻，可以通過工具欄上的按鈕進行各種操作。
4.  默認情況下，瀏覽器裡提供的是功能菜單，如果想使用瀏覽器自帶菜單，可以在首選項中設置，重新打開後生效。

![](https://github.com/aquariusStudio/cicada/blob/main/help/images/browserView.png)

**使用表格查看視頻信息：**

1.  表格更適合於顯示大量數據，考慮到一般視頻網站規模不算誇張，所以會將所有視頻信息全部在列表中展示，由於使用了NatTable，除了從數據庫加載時需要3秒左右，略慢，其餘時間均可以流暢操作。
2.  為了方便查詢，表格標題行的下一行是用來支持對每一列的查詢，包括時長等。
3.  表格支持多選，可以使用Shift/Ctrl等鍵進行多項選擇。
4.  點擊表格的標題頭可以進行排序。
5.  在表格的標題欄點擊鼠標右鍵，可以定制各欄的顯示狀態。
6.  表格支持編輯功能，但默認是關閉的，防止誤操作。如果確認有編輯功能的需要，可以在工具欄上點擊"解鎖"，然後即可編輯表格。對於修改的內容，可以通過"保存"操作將修改後的數據寫回數據庫。同樣，如果點擊"加鎖"關閉編輯功能時，也會自動將數據寫回數據庫。
7.  當用戶選中一行數據後，會在"屬性"視圖裡顯示該視頻的詳細信息。
8.  在表格里可以使用Tooltip的方式，通過鼠標在某條數據上停留1-2秒獲得詳細信息以及圖片。該功能默認關閉，可以在首選項中設置後使用，自動生效。

![](https://github.com/aquariusStudio/cicada/blob/main/help/images/tableView.png)