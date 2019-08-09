package facele.cl.mypymepos.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

import com.hisense.pos.apdutrans.ApduResp;
import com.hisense.pos.apdutrans.ApduSend;
import com.hisense.pos.ic.IcCard;
import com.hisense.pos.led.Led;
import com.hisense.pos.magic.MagCard;
import com.hisense.pos.magic.TrackResult;
import com.hisense.pos.ped.PINDATA;
import com.hisense.pos.ped.Ped;
import com.hisense.pos.picc.PiccCard;
import com.hisense.pos.port.CommPort;
import com.hisense.pos.scanner.Scanner;
import com.hisense.pos.spiprinter.SpiPrinter;
import com.hisense.pos.system.LocalSys;
import com.hisense.pos.system.Sys;
import com.hisense.pos.util.BytesUtil;

import java.util.Arrays;


public class SDKAPI {
    private IcCard ic = null;
    private MagCard mag = null;
    public PiccCard picc = null;
    private CommPort commport = null;
    public Scanner scanner = null;
    public Ped ped = null;

    private Led led = null;


    public SDKAPI() {
        ic = new IcCard();
        mag = new MagCard();
        picc = new PiccCard();
        commport = new CommPort();
        scanner = new Scanner();
        ped = new Ped();
        led = new Led();
    }

    //ICCARD 20170209 songlin
    public int setIccPowerUp(byte slot, byte[] ATR) {
        return ic.Icc_Init((byte) 0, slot, ATR);
    }

    public int setIccPowerDown(byte slot) {
        return ic.Icc_Close(slot);
    }

    public int detectIcCard(byte cardtype, byte slot) {
        return ic.Icc_Detect(cardtype, slot);
    }

    int iccCmdInteraction(byte slot, ApduSend ApduSend, ApduResp ApduRecv) {
        return ic.Icc_IsoCommand(slot, ApduSend, ApduRecv);
    }

    //MagCard
    public int openMagReader() {
        return mag.Mag_Open();
    }

    public int closeMagReader() {
        return mag.Mag_Close();
    }

    public int resetMagReader() {
        return mag.Mag_Reset();
    }

    public int isMagSwiped() {
        return mag.Mag_Swiped();
    }

    public int readMagData(TrackResult result) {
        return mag.Mag_Read(result);
    }

    //PiccCard
    public int setPiccPowerUp() {
        return picc.piccOpen();
    }

    public int detectPicCard(byte Mode, byte[] CardType, byte[] SerialInfo, byte[] CID, byte[] Other) {
        return picc.piccDetect(Mode, CardType, SerialInfo, CID, Other);
    }

    public void setPiccPowerDown() {
        picc.piccClose();
    }


    //Port
    public int portConnect(int port, int iBaudrate, byte[] comFmt) {
        return commport.PortConnect(port, iBaudrate, comFmt);
    }

    public int portSend(int port, byte[] value, int iLen) {
        return commport.Port_Send(port, value, iLen);
    }

    public int portRecv(int port, byte[] value, int len, long timeout) {
        return commport.Port_Recv(port, value, len, timeout);
    }

    public int portClose(int port) {
        return commport.Port_Close(port);
    }

    public int portClrBuf(int port) {
        return commport.Port_ClrBuf(port);
    }

    public int portFlush(int port) {
        return commport.Port_Flush(port);
    }

    //Scanner
    boolean openPosScanBar() {
        int iRet = scanner.Scanner_Open();
        if (iRet == scanner.SCANNER_OK) {
            return true;
        } else {
            return false;
        }
    }

    boolean closePosScanBar() {
        int iRet = scanner.Scanner_Close();
        if (iRet == scanner.SCANNER_OK) {
            return true;
        } else {
            return false;
        }
    }

    int startScanBar() {
        return 1;
    }

    int stopScanBar() {
        return 1;
    }

    int readPosScanBar(byte[] value, int len) {
        return scanner.Scanner_ReadData(value, len);
    }

    //pinped
    String getPedVer() {
        byte[] ver = new byte[30];
        int iRet = ped.Ped_GetVer(ver);
        if (iRet == ped.PED_OK) {
            String strVer = new String(ver);
            return strVer;
        } else {
            return null;
        }
    }

    int getPedErrCode() {
        return ped.Ped_GetErrCode();
    }

    int initPed(byte ucType) {
        byte buf = 0;
        return ped.Ped_Init(buf);
    }

    boolean setPinKeyboardMode(int mode) {
        return ped.setPinKeyboardMode(mode);
    }

    int writeMainKey(byte ucKeyIdx, byte[] pszKeyData, int iKeyDataLen) {
        return ped.Ped_WriteMKey(ucKeyIdx, pszKeyData, iKeyDataLen);
    }

    int writeWorkKey(byte ucMasterKeyIndex, byte ucWKeyIndex, byte[] psKeyData, int iKeyDataLen, byte ucDesType, byte ucKeyType) {
        return ped.Ped_WriteWKey(ucMasterKeyIndex, ucWKeyIndex, psKeyData, iKeyDataLen, ucDesType, ucKeyType);
    }

    int getPin(String Info, String strAmount, byte Mode, byte[] pszPAN, byte ucMinPINLen, byte ucMaxPINLen, int usTimeOut, byte ucKeyIndex, byte ucDesType, boolean bypassPin, String pedType, PINDATA pindata) {
        return ped.Ped_GetPin(Info, strAmount, Mode, pszPAN, ucMinPINLen, ucMaxPINLen, usTimeOut, ucKeyIndex, ucDesType, bypassPin, pedType, pindata);


    }

    int calculateMac(byte ucCalMode, byte[] psMAC, byte[] psData, int iDataLen, byte ucKeyIdx, byte ucDesType, byte[] psInitVct) {
        return ped.Ped_GetMac(ucCalMode, psMAC, psData, iDataLen, ucKeyIdx, ucDesType, psInitVct);
        //return ped.getmac(psMAC,  psData, iDataLen,  ucKeyIdx,psInitVct);
    }

    int encryptByTDK(byte KeyIdx, byte[] Input, byte[] Output, byte Algorithm) {
        return ped.Ped_EncryptByTDK(KeyIdx, Input, Output, Algorithm);
    }

    int encryptByKey(byte[] psInData, byte[] psOutData, byte ucMainKeyIdx, byte[] psCryptKey, byte ucDesType) {
        return ped.Ped_EncryptByKey(psInData, psOutData, ucMainKeyIdx, psCryptKey, ucDesType);
    }

    int writeEncryptMKey(byte ucKeyIdx, byte[] pszKeyData, int iKeyDataLen, byte DeccryptionKeyIndex) {
        return ped.Ped_WriteEncryptMKey(ucKeyIdx, pszKeyData, iKeyDataLen, DeccryptionKeyIndex);
    }

    int checkKeyStatus(byte ucKeyType, byte ucKeyIdx) {
        return ped.Ped_CheckKeyStatus(ucKeyType, ucKeyIdx);
    }

    int deleteAppointKey(byte ucKeyType, byte ucKeyIdx) {
        return ped.Ped_DeleteAppointKey(ucKeyType, ucKeyIdx);
    }

    int formatKeyArea() {
        return ped.Ped_formatKeyArea();
    }

    int desEncByKey(byte ucKeyType, byte ucKeyIdx, byte ucAlgorithmPattern, byte ucDesType, byte[] psInData, byte[] psOutData) {
        return ped.Ped_desEncByKey(ucKeyType, ucKeyIdx, ucAlgorithmPattern, ucDesType, psInData, psOutData);
    }

    private boolean cmpByteArray(byte[] kcv, byte[] checkValue) {
        for (int i = 0; i < checkValue.length; i++) {
            if (checkValue[i] != kcv[i]) {
                return false;//��У��ʧ��
            }
        }

        return true;
    }


    public boolean loadWorkKey(int keyType, int mkId, int wkId, byte[] key,
                               byte[] checkValue) throws RemoteException {

        if (mkId < 0 || mkId > 99 || wkId < 0 || wkId > 99) {
            return false;
        }
        if (key == null) {
            return false;
        }

        byte[] workKey = new byte[16];
        if (key.length == 8) {
            System.arraycopy(key, 0, workKey, 0, 8);
            System.arraycopy(key, 0, workKey, 8, 8);
        } else {
            workKey = key;
        }

        int ret = 0;
        if (null != checkValue) {
            byte[] initValue = new byte[8];
            Arrays.fill(initValue, (byte) 0x00);
            byte[] calcValue = new byte[8];
            ret = ped.Ped_EncryptByKey(initValue, calcValue, (byte) (mkId), workKey, (byte) 2);
            if (ret != Ped.PED_OK || !cmpByteArray(calcValue, checkValue)) {
                return false;
            }
        }

        int ihi98keyType = 0;
        if (keyType == 1) {
            ihi98keyType = 2;
        } else if (keyType == 2) {
            ihi98keyType = 1;
        } else if (keyType == 3) {
            ihi98keyType = 3;
        }

        ret = ped.Ped_WriteWKey((byte) (mkId), (byte) (wkId), workKey, workKey.length, (byte) 2, (byte) ihi98keyType);
        if (ret == Ped.PED_OK) {
            return true;
        } else {
            return false;
        }

    }

    public byte[] getParaPan(String account) {

        return ped.getPanDataFromAccount(account);

    }

    //System

    private static SDKAPI sdkapi = null;

    public static SDKAPI getInstance() {
        if (sdkapi == null) {
            sdkapi = new SDKAPI();
        }
        return sdkapi;
    }

    private Sys sys = null;

    public void setContext(Context mContext) {
        sys = new Sys(mContext);
    }

    public String getSysVersion() {
        return "V1.00.170120.SYS";
    }

    public int sysBeep(int period, int beepNo) {
        return sys.Sys_Beep(period, beepNo);

    }

    public String readSysSN(int type) {

        byte[] data = new byte[128];
        int iret = sys.Sys_ReadSN(data);

        if (iret > 0) {
            return new String(data);
        }
        return null;
    }

    public String readSysDeviceTypeAndModel() {

        boolean bRet = sys.Sys_IsExist_Snkey();

        byte[] ucDevType = new byte[128];
        byte[] ucDevModel = new byte[128];
        int iret = sys.Sys_GetDeviceTypeAndModel(ucDevType, ucDevModel);

        if (iret > 0) {
            if (bRet == true)
                return "DevType:" + new String(ucDevType).trim() + ",DevModel:" + new String(ucDevModel).trim() + " snkey exist!";
            else
                return "DevType:" + new String(ucDevType).trim() + ",DevModel:" + new String(ucDevModel).trim() + " snkey not exist!";
        }
        return null;
    }

    public String readCUPDeviceSn() {

        byte[] data = new byte[50];
        int iCUPDeviceSnLen = sys.Sys_GetCUPDeviceSn(data, (byte) 0);

        if (iCUPDeviceSnLen > 0) {
            Log.d("----------------------", "-------------" + new String(data).trim().length());
            Log.d("----------------------", new String(data).trim());
            return new String(data).trim();
        }
        return null;
    }

    public byte[] readEncryptCUPDeviceSn(byte[] pucCUPDevSn, byte[] pucEncryptFactor, int iEncryptFactorLen) {

        byte[] data = new byte[8];
        data = sys.Sys_GetEncryptCUPDeviceSn(pucCUPDevSn, pucEncryptFactor, iEncryptFactorLen);
        return data;

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    private LocalSys sysl = new LocalSys();

    public int writeSysMac(byte[] pMac) {
        return 0;
    }

    public int setSysClock(byte[] pDate) {
        byte[] pOut = BytesUtil.asc2BCDBytes(pDate);
        return sys.Sys_SetTime(pOut);

    }

    public byte[] getSysRandom() {

        byte[] data = new byte[8];
        if (sys.Sys_GetRandom(data) < 0) {
            return null;
        } else {
            return data;
        }
    }

    public int[] getSysRandom0to9() {
        int[] data = new int[10];
        int x, idatavaildNum = 0;

        byte[] pucData = new byte[20];

        int iFlag = 0;
        int i = 0, k = 0, m = 1;
        int iRandDataLen = 20;
        boolean bFull = false;

        while (true) {
            iFlag = sysl.localSys_GetRandom(pucData, iRandDataLen);
            for (i = 0; i < iRandDataLen; i++) {
                x = (int) (pucData[i] & 0X1F) % 10;
                if (idatavaildNum == 0) {
                    data[idatavaildNum] = x;
                    idatavaildNum++;
                    continue;
                }
                for (k = 0; k < idatavaildNum; k++) {
                    if (data[k] == x)
                        break;
                }
                if (idatavaildNum == 10) {
                    bFull = true;
                    break;
                }
                if (k == idatavaildNum) {
                    data[idatavaildNum] = x;
                    idatavaildNum++;

                }

            }
            if (bFull == true) {
                break;
            }
        }
        return data;


    }

    public byte[] getSysClock() {

        byte[] time = new byte[7];
        if (sys.Sys_GetTime(time) < 0) {
            return null;
        } else {
            return BytesUtil.bcdBytes2Asc(time);
        }
    }

    //Printer
    private SpiPrinter prn = new SpiPrinter();

    public String getPrnVersion() {
        return "V1.00.170120.PRN";
    }

    public int initPrinter() {
        return prn.Printer_init();
    }

    public int setPrnGray(int level) {
        return prn.Printer_setGray(level);
    }

    public int getPrnStatus() {
        return prn.Printer_getStatus();
    }

    public int setPrnText(String text, int fontSize, int pattern) {
        return prn.Printer_TextStr(text, fontSize, 0, pattern);
    }

    public int setPrnBitmap(Bitmap bitmap, int pattern) {
        return prn.Printer_Image(bitmap, pattern);
    }

    public int setPrnSignatureBitmap(Bitmap bitmap, int pattern) {
        return prn.Printer_SignatureImage(bitmap, pattern, 200);
    }

    public int setPrnColorBitmap(Bitmap bitmap, int pattern) {
        return prn.Printer_ColorImage(bitmap, pattern);
    }

    public int feedPaper(int value, int unit) {
        return prn.Printer_feedPaper(value, unit);
    }

	/*public int clrPrnBuffer(){
		return prn.Printer_clearBuf();
	}*/

    public int cutPaper() {
        return prn.Printer_cutPaper();
    }

    public int startPrint() {
        int iret = prn.Printer_Start();
        switch (iret) {
            case 0:
                return 0;
            case -4:
                return -51;
            case -5:
                return -52;
            case -6:
                return -53;
        }
        return -50;
    }

    //cashbox
    public String getCashDrawerVer() {
        return "V1.00.170120.CASHDR";
    }

    public int openCashDrawer(int id) {
        return 0;

    }

    public int getCashDrawerStatus() {
        return 0;
    }

    //LED
    public int setLEDStatus(byte whichOne, byte stat, byte internal, byte duration) {
        return led.setStatus(whichOne, stat, internal, duration);
    }

    public static void logHex(String tag, byte[] bytes, int len) {
        int min = Math.min(bytes.length, len);

        String str = BytesUtil.bytes2HexString(BytesUtil.subBytes(bytes, 0, min));
        Log.d("MainActivity", tag + str);
    }

    public static String bytes2HexString(byte[] bytes, int len) {
        int min = Math.min(bytes.length, len);

        String str = BytesUtil.bytes2HexString(BytesUtil.subBytes(bytes, 0, min));
        return str;

    }


}
