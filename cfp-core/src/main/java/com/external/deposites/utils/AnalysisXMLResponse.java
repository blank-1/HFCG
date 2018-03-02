package com.external.deposites.utils;

public class AnalysisXMLResponse {
        private String xml;
        private String retPlainStr;
        private String retSignatureStr;
        private boolean verifyPass;

        public AnalysisXMLResponse(String xml) {
            this.xml = xml;
        }

        public String getRetPlainStr() {
            return retPlainStr;
        }

        public String getRetSignatureStr() {
            return retSignatureStr;
        }

        public boolean isVerifyPass() {
            return verifyPass;
        }

        public AnalysisXMLResponse invoke() {
            final String plainStartEle = "<plain>";
            final String plainEndEle = "</plain>";
            retPlainStr = xml.substring(xml.indexOf(plainStartEle), xml.indexOf(plainEndEle) + 8);
            retSignatureStr = xml.substring(xml.indexOf("<signature>") + 11, xml.indexOf("</signature>"));
            // 验签结果
            verifyPass = SecurityUtils.verifySign(retPlainStr, retSignatureStr);
            return this;
        }
    }