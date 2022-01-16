import { NativeModules } from 'react-native';
var sdkManager = NativeModules.ArmfitSdk;
class ArmfitSdkManager {
  constructor() {
    // this.isPeripheralConnected = this.isPeripheralConnected.bind(this);
  }

  startSdk(): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.start((error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }

  stopScan(): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.stopScan((error: any) => {
        if (error != null) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }

  // Scaning code to connect to BLE
  scan(scanningOptions: any): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      if (scanningOptions == null) {
        scanningOptions = {};
      }
      if (scanningOptions.numberOfMatches == null) {
        scanningOptions.numberOfMatches = 3;
      }

      // (ANDROID) Defaults to MATCH_MODE_AGGRESSIVE
      if (scanningOptions.matchMode == null) {
        scanningOptions.matchMode = 1;
      }

      // (ANDROID) Defaults to SCAN_MODE_LOW_POWER on android
      if (scanningOptions.scanMode == null) {
        scanningOptions.scanMode = 0;
      }

      if (scanningOptions.reportDelay == null) {
        scanningOptions.reportDelay = 0;
      }
      sdkManager.scan([], 5, true, scanningOptions, (error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }

  connect(peripheralId: any): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.connect(peripheralId, (error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }

  retrieveServices(peripheralId: any, services?: any): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.retrieveServices(
        peripheralId,
        services,
        (error: any, peripheral: any | void | PromiseLike<string | void>) => {
          if (error) {
            reject(error);
          } else {
            fulfill(peripheral);
          }
        }
      );
    });
  }

  getInfo(): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.getInfo((error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }

  getData(): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.getData((error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }

  getRealTimeData(): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.getRealTimeData((error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }
  getFiles(): Promise<void | string> {
    return new Promise((fulfill, reject) => {
      sdkManager.getFiles((error: any) => {
        if (error) {
          reject(error);
        } else {
          fulfill();
        }
      });
    });
  }
}

export default new ArmfitSdkManager();
