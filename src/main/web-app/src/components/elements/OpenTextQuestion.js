export default function OpenTextQuestion({element, inputs, setInputs}) {

    function onChangeHandler(e) {
        const key = parseInt(element.id)
        setInputs({...inputs, [key]: e.target.value})
    }

    return (
        <div><p><b>
            {element.question}
        </b>
        </p>
            <p>
                <input className="form-control" type='text' value={inputs[element.id] || ''} onChange={onChangeHandler}
                       id={element.id}/>
            </p>
        </div>
    )
}