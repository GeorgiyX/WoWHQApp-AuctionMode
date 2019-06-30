package com.example.wowhqapp.presenters;

import com.example.wowhqapp.contracts.MainContract;
import com.example.wowhqapp.repositories.SettingRepository;
import com.example.wowhqapp.repositories.WoWTokenServiceRepo;

public class WoWTokenServicePresenter implements MainContract.WoWTokenServicePresenter {


    private SettingRepository mSettingRepository;
    private MainContract.WoWTokenService mWoWTokenService;
    private MainContract.TokenServiceRepository mTokenServiceRepository;
//    private boolean isThreadWork;
    private long last_notification_price;
    private long target_price;
    private boolean target_price_sign;
    private boolean is_check_target_price;
//    private Timer mTokenScanerTimer;


    public WoWTokenServicePresenter(SettingRepository settingRepository, MainContract.WoWTokenService woWTokenService){
        mSettingRepository = settingRepository;
        mWoWTokenService = woWTokenService;
        mTokenServiceRepository = new WoWTokenServiceRepo(mSettingRepository.getRegion());

        //Больше/Меньше
        target_price_sign = mSettingRepository.getTargetPriceSig();
        //Желаемая цена
        target_price = mSettingRepository.getTargetPrice();
        //Чтобы не слать одинаковые уведомления
        last_notification_price = 0;
        //Т.к. если что-то изменится то произойдет рестарт сервиса и значение обновится
        is_check_target_price = mSettingRepository.getWoWTokenServiceEnable();
        //Запихнул его сюда т.к. нужно его еще и завершать
//        mTokenScanerTimer = new Timer();
    }

    @Override
    public boolean init() {
        scanWoWToken();
//        if ((is_from_activity || mSettingRepository.getWoWTokenServiceEnable()) && !isThreadWork){ //Проверяем можноли работать сервису
//            isThreadWork = true;
//            mTokenScanerTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    scanWoWToken();
//                }
//            }, 100L, 8L*1000);
//        }
//        else{
//            mWoWTokenService.stopService();
//        }
        return true;
    }

    @Override
    public void destroy() {
//        mTokenScanerTimer.cancel();
    }

    private void scanWoWToken(){
        long current_price = mTokenServiceRepository.saveWoWTokenAndGetCurrentPrice();

        //Смотрим, делать ли уведомление
        if (is_check_target_price && (last_notification_price != current_price) &&
                ((target_price > current_price && !target_price_sign)
                        || (target_price < current_price && target_price_sign)
                        || target_price == current_price)){

            mWoWTokenService.makeNotification(current_price, mSettingRepository.getRegion());
            last_notification_price = current_price;
        }
    }
}
