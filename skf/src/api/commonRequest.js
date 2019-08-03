import request from '../api/request'

export function getServerBusList(condition)
{
    let params = new URLSearchParams();
    if (condition) {
        let keys = Object.keys(condition);
        for (let key of keys) {
            params.append(key, condition[key]);
        }
    }
    return new Promise((resolve, reject) => {
        return request({
            url: `${HOST}bus/base/info/list`,
            method: 'post',
            data: params
        }).then(response=> {
            resolve(response);
        }).catch(error=> {
            reject(error);
        })
    })
}

export function getServerBusStati(condition)
{
    let params = new URLSearchParams();
    if (condition) {
        let keys = Object.keys(condition);
        for (let key of keys) {
            params.append(key, condition[key]);
        }
    }
    return new Promise((resolve, reject) => {
        return request({
            url: `${HOST}bus/stations/list`,
            method: 'post',
            data: params
        }).then(response=> {
            resolve(response);
        }).catch(error=> {
            reject(error);
        })
    })
}