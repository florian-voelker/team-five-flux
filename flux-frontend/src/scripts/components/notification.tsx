import { connect } from "react-redux";
import React from "react";
import { State } from "../store/state-types";
import { DismissNotificationAction } from "../store/reducer-notification";

interface NotificationProps {
	tenMinutes: boolean,
	dismissNotificationHandler: () => void
}

export const Notification = (props: NotificationProps) => {
	console.log(props);
	console.log(props.tenMinutes);
	
	const notification = 
		<div id="notification" onClick={()=>props.dismissNotificationHandler()}>
			<h4>
				Du spielst jetzt seit 10 Minuten!
			</h4>
		</div> 

	return props.tenMinutes?notification:<></>;
}

function dismissNotificationActionCreator():DismissNotificationAction {
	return {type:"DISMISS_NOTIFICATION"};
}


export default connect(
	(state: State) => ({tenMinutes: state.tenMinutes}),
	{dismissNotificationHandler: dismissNotificationActionCreator}
)(Notification);