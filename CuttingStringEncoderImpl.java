import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

// ��� ������� �� �������, �������������� ������ ������� ���������� �� ������, ��� ��� ��� ��������� �������� ������ ����� ������ ��������
public class CuttingStringEncoderImpl implements CuttingStringEncoder {
	
    private int y = 1;                                      // ����� ���������� ���������
    private int capY = 1;                                   // �������� ����������� Y
    private String stY = "1";                               // ����� ���������� ��������� � ���� ������ � ���������� ������
    private String stYY = "1"; 								// ����� ���������� ��������� � ���� ������ � ���������� ������ (��� y-1)
    boolean flagExit = false;								// ����, ��� ����� ������ ������� ���� ������
    boolean finalLoop = false;								// ���� ���������� ������� �������� �����, ����� Y ��� �������� � ��� �� �������

    private void incY() {
    // ����� ��� ���������� ������ ���������� ��������� Y
    	
    if (finalLoop) { 										// � ��������� ������ �������� ����� Y ��� �������� � ��� �� �������
    	//System.out.println("��������� ������ �������� �����");
    }else{
		y++;
		stY = String.valueOf(y);
		stYY = String.valueOf(y-1);
		while (capY > (stY.length())) {
			stY = "0" + stY;
		}
		while (capY > (stYY.length())) {
			stYY = "0" + stYY;
		}
		if (stY.length() > stYY.length()){
			capY++;
			flagExit = true;								//  � ���� ������ ����� ������ ������� ���� ������, ��� ��� �������� ����������� Y �����������
			y = 1;
			stY = "1";
			while (capY > (stY.length())) {
				stY = "0" + stY;
				stYY = "0" + stYY;
			}
			//System.out.println("�������� ������� ���� ������");
		}
    }
	}
    
    private void getByteAndAddRes(String str, Charset charset, List<byte[]> res) throws IllegalArgumentException {
    	// ����� ��� �������� �������� ��������, ������� ��� � �������� ���������. ���� ������� ���, �� ��������� ������� � ����� � ���������� � �������������� ������ 
    	
        CharsetEncoder encoder = charset.newEncoder();
        if (!encoder.canEncode(str)){
        	throw new IllegalArgumentException("������� " + str +  " ��� � �������� ���������");
        }
        res.add(str.getBytes(charset));
    }
	
	@Override
	public List<byte[]> cutAndEncode(String source, Charset charset,
			int maxSegLen) throws IllegalArgumentException {
		
           List<byte[]> res = new ArrayList<byte[]>();    // ���������
		
			// �������� �������� ����������
		    // !!!�������� ����� �������� ����� ����� �������� �� ������ ��������, ����� �� ��� ���������� ������� � ������ �� �������� �� ��� ���������
			if (source == null || charset == null) {
				throw new IllegalArgumentException("���� ������ ���������!");
			} else if (maxSegLen < 6) {
					throw new IllegalArgumentException("������ �������� ������ ����� ����!");
			/*} else if (!Charset.isSupported(charset.name())) {
				throw new IllegalArgumentException("��������� �� �������������� ��������!");*/
			} else {
				
                                //��������� �������� ������ �� ������ ����
                                String[] sourcespl = source.split(" ");			// ������ �������� ����
          
                                // v.1 �������� ��������� �� ���� (��� �������) (��������� � ��������)
                                /*for (int i=0; i<sourcespl.length ;i++){
                                    if (!sourcespl[i].isEmpty()) {          // � ���������� ������ ����� ���� ������ ����� - �� �� �������
                                
                                        System.out.println("tmp: " + tmp);       
                                        
                                        if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).length() <= maxSegLen) {
                                            tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];
                                        }else{                              // ���� ������� �� ������� �������� 
                                            if (tmp.isEmpty()) {                // ���� ��� ������������ ����� � ��������, 
                                                                                // �� ��� ��������� � ����� �������� ���� ����� �� ��������. � ������� �������� ������
                                                for (int j=0; j<(sourcespl[i].length())/maxSegLen; j++){
                                                    res.add(sourcespl[i].substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                }
                                                tmp = sourcespl[i].substring(sourcespl[i].length()-((sourcespl[i].length())%maxSegLen));
                                            }else{
                                                res.add(tmp);                   // ���������� ������� � ������������
                                                i--;
                                                tmp = "";
                                            }
                                        }
                                        
                                    }
                                }*/
                                
                                // v.2 �������� ��������� �� ���� (� �������� (Y ���� ���������)) (��������� � ��������)
                                /*for (int i=0; i<sourcespl.length ;i++){
                                    if (!sourcespl[i].isEmpty()) {          // � ���������� ������ ����� ���� ������ ����� - �� �� �������
                                
                                        System.out.println("tmp: " + tmp);    
                                        //System.out.println("res.size(): " + res.size());  
                                        
                                        if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).length() <= (maxSegLen-((res.size())/10)-4)) { //(/Y ���� ���������)
                                            tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];
                                            if (i == (sourcespl.length-1)) {// ����  ������� ���������, �� �����
                                                res.add(tmp);
                                            }
                                        }else{                              // ���� ������� �� ������� �������� 
                                            if (tmp.isEmpty()) {                // ���� ��� ������������ ����� � ��������, 
                                                                                // �� ��� ��������� � ����� �������� ���� ����� �� ��������. � ������� �������� ������
                                            String lsource = "";                // ����� ������ ������ ����� � ���������    
                                            String index = "";                  // ����� ������ ������
                                            String end = "";                    // ����� ������ �����
                                                //if (res.size()>0) {                 // ���� ������� ��� �� ������, �� ����� ������ ���������� ������ ����� ������
                                                    for (int k=0; k<(sourcespl[i].length()); k++){    // ����������� ������� ����� � lsource
                                                        if (k==0) {
                                                            lsource = res.size()+1 + "/Y ";
                                                        }
                                                        lsource = lsource + sourcespl[i].substring(k,k+1);
                                                        if ((lsource.length()%maxSegLen==0) && (k!=sourcespl[i].length()-1)) {
                                                            index = (res.size()+1 + (lsource.length()/maxSegLen)) + "/Y ";
                                                            lsource = lsource + index;
                                                        }
                                                    }
                                                    
                                                    System.out.println("lsource: " + lsource);
                                                            
                                                    for (int j=0; j<(lsource.length())/maxSegLen; j++){     // ��������� � ����� �������� ���� lsource �� ��������� ������ �����
                                                        res.add(lsource.substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                    }
                                                    System.out.println("��� ������ �� ������: " + index);
                                                    end = lsource.substring(lsource.length()-((lsource.length())%maxSegLen));
                                                    if ((i == (sourcespl.length-1)) && !(end.isEmpty())) {    // ����  ������� ���������, �� ����� �����
                                                        res.add(end);
                                                    }else{                                                   // ����  ������� �����������, �� ����� ������� � tmp                                                 
                                                        tmp = end.replaceFirst(index, "");
                                                    }
                                                //}else{
                                                //    for (int j=0; j<(sourcespl[i].length())/maxSegLen; j++){
                                                //        res.add(sourcespl[i].substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                //    }
                                                //    tmp = sourcespl[i].substring(sourcespl[i].length()-((sourcespl[i].length())%maxSegLen));
                                                //}
                                            }else{                              // ���������� ������� � �������� � ������������
                                                res.add(res.size()+1 + "/Y " + tmp);               
                                                i--;
                                                tmp = "";
                                            }
                                        }
                                        
                                    }
                                }*/                              
                                
                                // v.3 �������� ��������� �� ���� (��������� � ��������)
                                /*do {
                                
                                String tmp = "";                                // ����� ��������� ������ ��� ������������ ��������  
                                int totlength = 0;                              // ����� ���������� �������� � sourcespl
                                int totseghwoi = 0;                             // ����� ���������� ��������� ��� ��������
                                int totlengthwi = 0;                            // ����� ���������� �������� � ���������
                                res.clear();
                                
                                
                                flagExit = false;
                                
                                for (int i=0; i<sourcespl.length ;i++){
                                    if (!sourcespl[i].isEmpty()) {          // � ���������� ������ ����� ���� ������ ����� - �� �� �������
                                        
                                        if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).length() <= (maxSegLen-((res.size())/10) - 3 - capY )) {
                                            tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];   
                                            if (i == (sourcespl.length-1)) {				// ����  ������� ���������, �� �����
                                                res.add(res.size()+1 + "/" + stY + " " + tmp); 
                                                System.out.println("��������� ������� ");
                                                if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                            }
                                        }else{                              // ���� ������� �� ������� �������� 
                                            if (tmp.isEmpty()) {                // ���� ��� ������������ ����� � ��������, 
                                                                                // �� ��� ��������� � ����� �������� ���� ����� �� ��������. � ������� �������� ������
                                            String lsource = "";                // ����� ������ ������ ����� � ���������    
                                            String index = "";                  // ����� ������ ������
                                            String end = "";                    // ����� ������ �����
                                                    for (int k=0; k<(sourcespl[i].length()); k++){    // ����������� ������� ����� � lsource
                                                        if (k==0) {
                                                            lsource = res.size()+1 + "/" + stY + " ";
                                                        }
                                                        lsource = lsource + sourcespl[i].substring(k,k+1);
                                                        if (lsource.length()%maxSegLen==0) {
                                                        	incY(); if (flagExit) {break;}
                                                        	if (k!=sourcespl[i].length()-1) {	// ���� ������� ����� ������ ��������� �� ��������, �� � ���� � ����� �� ��������� ������
                                                        		index = (res.size()+1 + (lsource.length()/maxSegLen)) + "/" + stY + " ";
                                                            	lsource = lsource + index;
                                                        	}
                                                        }
                                                    }
                                                    
                                                    if (flagExit) {break;}
                                                    //System.out.println("lsource: " + lsource);
                                                            
                                                    for (int j=0; j<(lsource.length())/maxSegLen; j++){     // ��������� � ����� �������� ���� �� lsource �� ��������� ������ �����
                                                        res.add(lsource.substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                    }
                                                    //System.out.println("��� ������ �� ������: " + index);
                                                    end = lsource.substring(lsource.length()-((lsource.length())%maxSegLen));
                                                    if (!end.isEmpty()) {					// ���� ������� �����
                                                    	if (i == (sourcespl.length-1)) {    // ����  ������� ���������, �� ����� �����
                                                    		res.add(end);
                                                    		System.out.println("��������� ������� ");
                                                    		if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                                    	}else{                              // ����  ������� �����������, �� ����� ������� � tmp                                                 
                                                    		tmp = end.replaceFirst(index, "");
                                                    	}
                                                    }
                                            }else{                              // ���������� ������� � �������� � ������������
                                                res.add(res.size()+1 + "/" + stY + " " + tmp); incY();if (flagExit) {break;}               
                                                i--;
                                                tmp = "";
                                            }
                                        }
                                        
                                    }
                                }
                                
                                } while (flagExit);*/
                                
                                
                                // v.4 �������� ��������� �� ���� (��������� � ������)
                                // ��������, ��� ������� ��� ������� ("/ 0123456789") � ����� ��������� �������� 1 ���� 
                                
                                do {
                                    
                                    String tmp = "";                                // ����� ��������� ������ ��� ������������ ��������  
                                    res.clear();
                                    
                                    
                                    flagExit = false;
                                    
                                    for (int i=0; i<sourcespl.length ;i++){
                                        if (!sourcespl[i].isEmpty()) {          // � ���������� ������ ����� ���� ������ ����� - �� �� �������
                                            
                                            if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).getBytes(charset).length <= (maxSegLen - String.valueOf(res.size()).length() - 2 - capY )) { 
                                                tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];   
                                                if (i == (sourcespl.length-1)) {				// ���� ����� ���������, �� �����
                                                    getByteAndAddRes(res.size()+1 + "/" + stY + " " + tmp,charset,res); 
                                                    //System.out.println("��������� ������� ");
                                                    if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                                }
                                            }else{                              // ���� ������� �� ������� �������� 
                                                if (tmp.isEmpty()) {                // ���� ��� ������������ ����� � ��������, 
                                                                                    // �� ��� ��������� � ����� �������� ���� ������� ����� �� ��������. � ������� �������� ������
                                                String tmp2 = "";                   // ����� ��������� ������ ��� ������������ �������� �� �������� �����    
                                                		for (int k=0; k<(sourcespl[i].length()); k++){	// ������ ������� ����� �����������
                                                            if (k==0) {
                                                            	tmp2 = res.size()+1 + "/" + stY + " ";
                                                           }
                                                            if (tmp2.getBytes(charset).length + (sourcespl[i].substring(k,k+1)).getBytes(charset).length <= maxSegLen) {
                                                            	tmp2 = tmp2 + sourcespl[i].substring(k,k+1);
                                                            	 if (k == (sourcespl[i].length()-1)) {				// ���� ������� � ������� ����� ���������, �� �����
                                                                     //System.out.println("��������� ������� ");
                                                            		 if (i == (sourcespl.length-1)) {					// ���� ����� ���������
                                                                         getByteAndAddRes(tmp2,charset,res); 
                                                                         if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                                            		 }else{												// ���� ����� �� ���������
                                                            			 tmp = tmp2.replace(res.size()+1 + "/" + stY + " ", ""); 
                                                            		 }
                                                                 }
                                                            }else{                              				// ���� ������� �� ������� ��������, �� ���������� ������� � ������������ 
                                                            	getByteAndAddRes(tmp2,charset,res); incY();if (flagExit) {break;}               
                                                                k--;
                                                                tmp2 = res.size()+1 + "/" + stY + " ";
                                                            }
                                                		}
                                                }else{                              // ���������� ������� � �������� � ������������
                                                	getByteAndAddRes(res.size()+1 + "/" + stY + " " + tmp,charset,res); incY();if (flagExit) {break;}               
                                                    i--;
                                                    tmp = "";
                                                }
                                            }
                                            
                                        }
                                    }
                                    
                                    } while (flagExit);
				
				return res;
			}

	}

}
