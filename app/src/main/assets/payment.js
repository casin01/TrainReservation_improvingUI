var payment = {
paytest :function (){
        var IMP = window.IMP // 생략가능
        IMP.init('imp33835104') // 'iamport' 대신 부여받은 "가맹점 식별코드"를 사용

        requestPay function : () {
            // IMP.request_pay(param, callback) 결제창 호출
            /*
            IMP.request_pay({ // param
                pg: "html5_inicis",
                pay_method: "card",
                merchant_uid: "ORD220306-0011",
                name: "노르웨이 회전 의자",
                amount: 64900,
                buyer_name: "홍길동",
                buyer_tel: "010-4242-4242",
                */
                Android.onClickPayment();

            }, function (rsp) { // callback
                if (rsp.success) {
                // 결제 성공 시: 결제 승인 또는 가상계좌 발급에 성공한 경우
                var msg = '결제가 완료되었습니다.';
                msg += '고유ID : ' + rsp.imp_uid;
                msg += '상점 거래ID : ' + rsp.merchant_uid;
                msg += '결제 금액 : ' + rsp.paid_amount;
                msg += '카드 승인번호 : ' + rsp.apply_num;
                alert(msg);
                // jQuery로 HTTP 요청
                    jQuery.ajax({
                        url: "{서버의 결제 정보를 받는 endpoint}", // 예: https://www.myservice.com/payments/complete
                        //cross-domain error가 발생하지 않도록 주의해주세요
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        data: {
                            imp_uid: rsp.imp_uid,
                            merchant_uid: rsp.merchant_uid
                        }
                    }).done(function (data) { // 응답 처리
                        switch (data.status) {
                            case "vbankIssued":
                                // 가상계좌 발급 시 로직
                                break;
                            case "success":
                                // 결제 성공 시 로직
                                break;
                        }
                    });
                } else {
                    alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
                }
            });
        }

        function takeResponseAndHandle(rsp) {
            if (rsp.success) {
                // 인증성공
                console.log(rsp.imp_uid);
                console.log(rsp.merchant_uid);

                Android.getData(rsp.imp_uid);
                //rsp.imp_uid는 인증 성공한 유저의 인증 키
            } else {
                // 인증취소 또는 인증실패
                var msg = '인증에 실패하였습니다.';
                msg += '에러내용 : ' + rsp.error_msg;

                alert(msg);
            }
        }
});
}