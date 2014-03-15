import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

// как следует из задания, результирующий массив обратно собираться не должен, так как при разбиении исходной строки часть данных теряется
public class CuttingStringEncoderImpl implements CuttingStringEncoder {
	
    private int y = 1;                                      // общее количество сегментов
    private int capY = 1;                                   // итоговая разрядность Y
    private String stY = "1";                               // общее количество сегментов в виде строки с начальными нулями
    private String stYY = "1"; 								// общее количество сегментов в виде строки с начальными нулями (для y-1)
    boolean flagExit = false;								// флаг, что нужно начать главный цикл заново
    boolean finalLoop = false;								// флаг последнего прогона главного цикла, когда Y уже известен и его не трогаем

    private void incY() {
    // метод для вычисления общего количества сегментов Y
    	
    if (finalLoop) { 										// в последний прогон главного цикла Y уже известен и его не трогаем
    	//System.out.println("последний прогон главного цикла");
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
			flagExit = true;								//  в этот момент нужно начать главный цикл заново, так как итоговая разрядность Y увеличилась
			y = 1;
			stY = "1";
			while (capY > (stY.length())) {
				stY = "0" + stY;
				stYY = "0" + stYY;
			}
			//System.out.println("начинаем главный цикл заново");
		}
    }
	}
    
    private void getByteAndAddRes(String str, Charset charset, List<byte[]> res) throws IllegalArgumentException {
    	// метод для проверки символов сегмента, которых нет в заданной кодировке. если таковых нет, то переводит сегмент в байты и записывает в результирующий массив 
    	
        CharsetEncoder encoder = charset.newEncoder();
        if (!encoder.canEncode(str)){
        	throw new IllegalArgumentException("Сиволов " + str +  " нет в заданной кодировке");
        }
        res.add(str.getBytes(charset));
    }
	
	@Override
	public List<byte[]> cutAndEncode(String source, Charset charset,
			int maxSegLen) throws IllegalArgumentException {
		
           List<byte[]> res = new ArrayList<byte[]>();    // результат
		
			// проверка входящих параметров
		    // !!!вероятно стоит написать более умную проверку на размер сегмента, чтобы он был достаточно большой и индекс не заполнял бы его полностью
			if (source == null || charset == null) {
				throw new IllegalArgumentException("Есть пустые параметры!");
			} else if (maxSegLen < 6) {
					throw new IllegalArgumentException("Размер сегмента меньше шести байт!");
			/*} else if (!Charset.isSupported(charset.name())) {
				throw new IllegalArgumentException("Кодировка не поддерживается системой!");*/
			} else {
				
                                //разбиваем исходную строку на массив слов
                                String[] sourcespl = source.split(" ");			// массив исходных слов
          
                                // v.1 собираем результат из слов (без индекса) (результат в символах)
                                /*for (int i=0; i<sourcespl.length ;i++){
                                    if (!sourcespl[i].isEmpty()) {          // в результате сплита могут быть пустые слова - их не смотрим
                                
                                        System.out.println("tmp: " + tmp);       
                                        
                                        if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).length() <= maxSegLen) {
                                            tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];
                                        }else{                              // если вылазим за границы сегмента 
                                            if (tmp.isEmpty()) {                // если это единственное слово в сегменте, 
                                                                                // то его разбиваем и пишем сегменты пока слово не кончится. а остаток передаем дальше
                                                for (int j=0; j<(sourcespl[i].length())/maxSegLen; j++){
                                                    res.add(sourcespl[i].substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                }
                                                tmp = sourcespl[i].substring(sourcespl[i].length()-((sourcespl[i].length())%maxSegLen));
                                            }else{
                                                res.add(tmp);                   // записываем сегмент и откатываемся
                                                i--;
                                                tmp = "";
                                            }
                                        }
                                        
                                    }
                                }*/
                                
                                // v.2 собираем результат из слов (с индексом (Y пока константа)) (результат в символах)
                                /*for (int i=0; i<sourcespl.length ;i++){
                                    if (!sourcespl[i].isEmpty()) {          // в результате сплита могут быть пустые слова - их не смотрим
                                
                                        System.out.println("tmp: " + tmp);    
                                        //System.out.println("res.size(): " + res.size());  
                                        
                                        if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).length() <= (maxSegLen-((res.size())/10)-4)) { //(/Y пока константа)
                                            tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];
                                            if (i == (sourcespl.length-1)) {// если  сегмент последний, то пишем
                                                res.add(tmp);
                                            }
                                        }else{                              // если вылазим за границы сегмента 
                                            if (tmp.isEmpty()) {                // если это единственное слово в сегменте, 
                                                                                // то его разбиваем и пишем сегменты пока слово не кончится. а остаток передаем дальше
                                            String lsource = "";                // здесь храним длиное слово с индексами    
                                            String index = "";                  // здесь храним индекс
                                            String end = "";                    // здесь храним хвост
                                                //if (res.size()>0) {                 // если сегмент уже не первый, то нужен индекс ПОЛУЧАЕТСЯ ИНДЕКС НУЖЕН ВСЕГДА
                                                    for (int k=0; k<(sourcespl[i].length()); k++){    // преобразуем длинное слово в lsource
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
                                                            
                                                    for (int j=0; j<(lsource.length())/maxSegLen; j++){     // разбиваем и пишем сегменты пока lsource не останется только хвост
                                                        res.add(lsource.substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                    }
                                                    System.out.println("что удалем из хвоста: " + index);
                                                    end = lsource.substring(lsource.length()-((lsource.length())%maxSegLen));
                                                    if ((i == (sourcespl.length-1)) && !(end.isEmpty())) {    // если  сегмент последний, то пишем хвост
                                                        res.add(end);
                                                    }else{                                                   // если  сегмент непоследний, то хвост запишем в tmp                                                 
                                                        tmp = end.replaceFirst(index, "");
                                                    }
                                                //}else{
                                                //    for (int j=0; j<(sourcespl[i].length())/maxSegLen; j++){
                                                //        res.add(sourcespl[i].substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                //    }
                                                //    tmp = sourcespl[i].substring(sourcespl[i].length()-((sourcespl[i].length())%maxSegLen));
                                                //}
                                            }else{                              // записываем сегмент с индексом и откатываемся
                                                res.add(res.size()+1 + "/Y " + tmp);               
                                                i--;
                                                tmp = "";
                                            }
                                        }
                                        
                                    }
                                }*/                              
                                
                                // v.3 собираем результат из слов (результат в символах)
                                /*do {
                                
                                String tmp = "";                                // здесь сохраняем данные для формирования сегмента  
                                int totlength = 0;                              // общее количество символов в sourcespl
                                int totseghwoi = 0;                             // общее количество сегментов без индексов
                                int totlengthwi = 0;                            // общее количество символов с индексами
                                res.clear();
                                
                                
                                flagExit = false;
                                
                                for (int i=0; i<sourcespl.length ;i++){
                                    if (!sourcespl[i].isEmpty()) {          // в результате сплита могут быть пустые слова - их не смотрим
                                        
                                        if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).length() <= (maxSegLen-((res.size())/10) - 3 - capY )) {
                                            tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];   
                                            if (i == (sourcespl.length-1)) {				// если  сегмент последний, то пишем
                                                res.add(res.size()+1 + "/" + stY + " " + tmp); 
                                                System.out.println("последний сегмент ");
                                                if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                            }
                                        }else{                              // если вылазим за границы сегмента 
                                            if (tmp.isEmpty()) {                // если это единственное слово в сегменте, 
                                                                                // то его разбиваем и пишем сегменты пока слово не кончится. а остаток передаем дальше
                                            String lsource = "";                // здесь храним длиное слово с индексами    
                                            String index = "";                  // здесь храним индекс
                                            String end = "";                    // здесь храним хвост
                                                    for (int k=0; k<(sourcespl[i].length()); k++){    // преобразуем длинное слово в lsource
                                                        if (k==0) {
                                                            lsource = res.size()+1 + "/" + stY + " ";
                                                        }
                                                        lsource = lsource + sourcespl[i].substring(k,k+1);
                                                        if (lsource.length()%maxSegLen==0) {
                                                        	incY(); if (flagExit) {break;}
                                                        	if (k!=sourcespl[i].length()-1) {	// если длинное слово удачно разбилось на сегменты, то к нему в конец не добавляем индекс
                                                        		index = (res.size()+1 + (lsource.length()/maxSegLen)) + "/" + stY + " ";
                                                            	lsource = lsource + index;
                                                        	}
                                                        }
                                                    }
                                                    
                                                    if (flagExit) {break;}
                                                    //System.out.println("lsource: " + lsource);
                                                            
                                                    for (int j=0; j<(lsource.length())/maxSegLen; j++){     // разбиваем и пишем сегменты пока от lsource не останется только хвост
                                                        res.add(lsource.substring(j*maxSegLen,j*maxSegLen+maxSegLen));
                                                    }
                                                    //System.out.println("что удалем из хвоста: " + index);
                                                    end = lsource.substring(lsource.length()-((lsource.length())%maxSegLen));
                                                    if (!end.isEmpty()) {					// если остался хвост
                                                    	if (i == (sourcespl.length-1)) {    // если  сегмент последний, то пишем хвост
                                                    		res.add(end);
                                                    		System.out.println("последний сегмент ");
                                                    		if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                                    	}else{                              // если  сегмент непоследний, то хвост запишем в tmp                                                 
                                                    		tmp = end.replaceFirst(index, "");
                                                    	}
                                                    }
                                            }else{                              // записываем сегмент с индексом и откатываемся
                                                res.add(res.size()+1 + "/" + stY + " " + tmp); incY();if (flagExit) {break;}               
                                                i--;
                                                tmp = "";
                                            }
                                        }
                                        
                                    }
                                }
                                
                                } while (flagExit);*/
                                
                                
                                // v.4 собираем результат из слов (результат в байтах)
                                // допустим, что символы для индекса ("/ 0123456789") в любой кодировке занимают 1 байт 
                                
                                do {
                                    
                                    String tmp = "";                                // здесь сохраняем данные для формирования сегмента  
                                    res.clear();
                                    
                                    
                                    flagExit = false;
                                    
                                    for (int i=0; i<sourcespl.length ;i++){
                                        if (!sourcespl[i].isEmpty()) {          // в результате сплита могут быть пустые слова - их не смотрим
                                            
                                            if (((tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i]).getBytes(charset).length <= (maxSegLen - String.valueOf(res.size()).length() - 2 - capY )) { 
                                                tmp = (tmp.isEmpty()?tmp:(tmp + " ")) + sourcespl[i];   
                                                if (i == (sourcespl.length-1)) {				// если слово последнее, то пишем
                                                    getByteAndAddRes(res.size()+1 + "/" + stY + " " + tmp,charset,res); 
                                                    //System.out.println("последний сегмент ");
                                                    if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                                }
                                            }else{                              // если вылазим за границы сегмента 
                                                if (tmp.isEmpty()) {                // если это единственное слово в сегменте, 
                                                                                    // то его разбиваем и пишем сегменты пока длинное слово не кончится. а остаток передаем дальше
                                                String tmp2 = "";                   // здесь сохраняем данные для формирования сегмента из длинного слова    
                                                		for (int k=0; k<(sourcespl[i].length()); k++){	// читаем длинное слово посимвольно
                                                            if (k==0) {
                                                            	tmp2 = res.size()+1 + "/" + stY + " ";
                                                           }
                                                            if (tmp2.getBytes(charset).length + (sourcespl[i].substring(k,k+1)).getBytes(charset).length <= maxSegLen) {
                                                            	tmp2 = tmp2 + sourcespl[i].substring(k,k+1);
                                                            	 if (k == (sourcespl[i].length()-1)) {				// если сегмент в длинном слове последний, то пишем
                                                                     //System.out.println("последний сегмент ");
                                                            		 if (i == (sourcespl.length-1)) {					// если слово последнее
                                                                         getByteAndAddRes(tmp2,charset,res); 
                                                                         if (finalLoop) {flagExit=false;} else {finalLoop = true; flagExit = true;}
                                                            		 }else{												// если слово не последнее
                                                            			 tmp = tmp2.replace(res.size()+1 + "/" + stY + " ", ""); 
                                                            		 }
                                                                 }
                                                            }else{                              				// если вылазим за границы сегмента, то записываем сегмент и откатываемся 
                                                            	getByteAndAddRes(tmp2,charset,res); incY();if (flagExit) {break;}               
                                                                k--;
                                                                tmp2 = res.size()+1 + "/" + stY + " ";
                                                            }
                                                		}
                                                }else{                              // записываем сегмент с индексом и откатываемся
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
