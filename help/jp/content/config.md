###設定手順

現在、ソフトウェアは4つの優先コンテンツの構成を提供しています。ここにいくつかの重要な構成項目があります。

**ダウンロード手順：**

1. ffmpegコマンドを指定します。HLSプロトコルを使用するストリーミングメディアの場合、ダウンロードが完了した後、ffmpegを使用してマージする必要があり、ここで対応するコマンドラインを構成できます。 ffmpegが現在の環境に追加されている場合は、デフォルトの構成を使用してください。それ以外の場合は、「C：\\ MyTools \\ ffmpeg \\ bin \\ ffmpeg.exe -f concat -safe 0 -i "{0}" -c copy "など、ffmpeg実行ファイルのパスを指定する必要があります。 {1} "、configure" {0} "と" {1} "を正しく設定してください。前者は対応するHLSリストファイルで、後者は出力メディアファイルです。これら2つのパラメータは次のように自動的に入力されます。システム。
2.呼び出し元のシステムコマンドを設定し、[HLSのマージ後にソースファイルを削除する]をオンにして、マージ後に冗長なHLSフラグメントファイルを自動的にクリアします。
3.多くのサイトではスレッド接続数に制限があるため、スレッド接続が多すぎるためにエラーが発生した場合は、下の表で指定したサイトの接続制限を設定できます。 [www.google.com]（http://www.google.com）の接続数が10の場合、システムが使用するため、サイトキーワードとしてgoogleまたはgoogle.comを使用するだけで済みます。文字列には判断方法が含まれていました。
    

**インターフェースの説明：**

1.システムリソースが高すぎる、またはシステムが不安定であると思われる場合は、「Chromiumカーネルを使用する」の構成をキャンセルし、再起動後にChrominumを無効にして、システムの安定性を向上させることができます。
2. Mp4でもHLSストリーミングメディアでも、直接再生できるプレーヤーはたくさんあります。このマシンのブラウザを直接使用してビデオを表示したい場合は、「プレーヤーの開始コマンド」を設定することで、この機能をすばやく有効にできます。 "。デフォルトではPotPlayerが使用されます。お気に入りのプレーヤーを自由に指定できます。実行前にシステムがリモート再生URLアドレスをここに自動的に入力するため、パラメータ "{0}"を正しい位置に配置するだけです。

「ビデオ」と「サイト」の管理は比較的単純であり、詳細には説明しません。