<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>导入/导出</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<input name="userFile" type="file"
       accept=".csv,text/plain,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
<script>
    $(function() {
        $("input[name=userFile]").on("change", function () {
            var formData = new FormData();
            var userFileObj = $("input[name=userFile]")[0].files[0];
            formData.append("userFile", userFileObj);
            $.ajax({
                url: '/teacher/upload.do',
                type: 'POST',
                data: formData,
                // async: false,
                contentType: false,
                processData: false,
                success: function (result) {
                    console.log(result);
                },
                error: function (data) {
                    console.log(result);
                }
            });
        });
    });
</script>
</body>
</html>